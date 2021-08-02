package com.example.demo.src.content;

import com.example.demo.src.content.model.GetContentDtlRes;
import com.example.demo.src.content.model.GetEpisodeRes;
import com.example.demo.src.content.model.GetReleaseRes;
import com.example.demo.src.keep.model.GetKeepRes;
import com.example.demo.src.keep.model.PatchKeepReq;
import com.example.demo.src.keep.model.PostKeepReq;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.List;

@Repository
public class ContentDao {
    private JdbcTemplate jdbcTemplate;

    @Autowired
    public void setDataSource(DataSource dataSource){
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    public GetContentDtlRes getContentDtl(int contentId, int profileId) {
        String getContentDtlQuery = "select c.contentId\n" +
                "     , c.contentTitle\n" +
                "     , IF((select count(k.keepId)\n" +
                "            from KEEP_TB k\n" +
                "            where k.profileId = ? and\n" +
                "                  k.contentId = c.contentId and\n" +
                "                  k.status = 'a'\n" +
                "         ) > 0\n" +
                "         , 'Y'\n" +
                "         , 'N'\n" +
                "         ) keepYn\n" +
                "     , (\n" +
                "        select r.rateGb\n" +
                "        from RATE_TB r\n" +
                "        where r.profileId = ? and\n" +
                "              r.contentId = c.contentId and\n" +
                "              r.status = 'a'\n" +
                "         ) rateGb\n" +
                "     , c.previewUrl\n"+
                "     , c.contentExp\n" +
                "     , cm.commDtlnm age\n" +
                "     , date_format(c.releasedAt, '%Y') releasedAt\n" +
                "     , max(e.season) season\n" +
                "     , (\n" +
                "        SELECT GROUP_CONCAT(p.peopleNm SEPARATOR ',') AS result\n" +
                "        FROM CONTENTS_PEOPLE_TB cp\n" +
                "            join PEOPLE_TB p on cp. peopleId = p.peopleid\n" +
                "        where\n" +
                "              cp.contentId = c.contentId and\n" +
                "              cp.peopleGb = 'A' and\n" +
                "              cp.status = 'A' AND\n" +
                "              c.status = 'A'\n" +
                "         ) actor\n" +
                "     , (\n" +
                "        SELECT GROUP_CONCAT(commDtlNm SEPARATOR ',') AS result\n" +
                "        FROM CONTENTS_GENRE_TB cg\n" +
                "                 join COMM_DTL_TB cm on cg.genreId = cm.commDtlcd\n" +
                "        where cg.contentId = c.contentId\n" +
                "          and cm.commCd = 'genre'\n" +
                "          and cg.status = 'A'\n" +
                "          AND c.status = 'A'\n" +
                "        )genre\n" +
                "     , (\n" +
                "        SELECT GROUP_CONCAT(cm.commDtlNm SEPARATOR ',') AS result\n" +
                "        FROM CONTENTS_CHARACTER_TB cc\n" +
                "                 join COMM_DTL_TB cm on cc.characterId = cm.commDtlcd\n" +
                "        where cc.contentId = c.contentId\n" +
                "          and cm.commcd = 'character'\n" +
                "          and cc.status = 'A'\n" +
                "          AND c.status = 'A'\n" +
                "        )charct\n" +
                "from CONTENTS_TB c\n" +
                "    left outer join EPISODE_TB e on c.contentId = e.contentId\n" +
                "    left outer join COMM_DTL_TB cm on c.ageCd = cm.commDtlcd and cm.commCd = 'ageSimple'\n" +
                "where c.contentId = ?\n";
        Object[] getContentDtlParams = new Object[]{profileId,profileId,contentId};

        return this.jdbcTemplate.queryForObject(getContentDtlQuery,
                (rs,rowNum)-> new GetContentDtlRes(
                        rs.getInt("contentId"),
                        rs.getString("contentTitle"),
                        rs.getString("keepYn"),
                        rs.getString("rateGb"),
                        rs.getString("previewUrl"),
                        rs.getString("contentExp"),
                        rs.getString("age"),
                        rs.getString("releasedAt"),
                        rs.getInt("season"),
                        rs.getString("actor"),
                        rs.getString("genre"),
                        rs.getString("charct")
                ),
                getContentDtlParams
        );

    }

    public List<GetEpisodeRes> getEpisode(int contentId, int profileId, int season) {
        String getEpisodeQuery = "select row_number() over (order by e.episodeNo) rowNUm\n" +
                "     , e.season\n" +
                "     , e.episodeId" +
                "     , concat(e.episodeNo,'화') episodeNo\n" +
                "     , concat(round(time_to_sec(e.runningTime)/60,0),'분') runningTime\n" +
                "     , e.summary\n" +
                "     , e.episodeImgUrl\n" +
                "     , w.wachedtime watchedTime\n" +
                "from EPISODE_TB e\n" +
                "    left outer join CONTENTS_TB c on c.contentId = e.contentId\n" +
                "    left outer join WATCH_RECODE_TB w on e.episodeId = w.episodeId and\n" +
                "                                         w.profileId = ? and\n" +
                "                                         w.status = 'A'\n" +
                "where e.contentId = ? and\n" +
                "      e.season = ?\n";
        Object[] getEpisodeParams = new Object[]{profileId,contentId,season};

        return this.jdbcTemplate.query(getEpisodeQuery,
                (rs,rowNum)-> new GetEpisodeRes(
                        rs.getInt("rowNum"),
                        rs.getInt("season"),
                        rs.getInt("episodeId"),
                        rs.getString("episodeNo"),
                        rs.getString("runningTime"),
                        rs.getString("summary"),
                        rs.getString("episodeImgUrl"),
                        rs.getString("watchedTime")
                ),
                getEpisodeParams
        );

    }

    public List<GetReleaseRes> getRelease(int profileId) {
        String getReleaseQuery = "select c.contentId\n" +
                "     , c.contentTitle\n" +
                "     , c.contentExp\n" +
                "     , c.previewUrl\n" +
                "     , IF(n.notId = null, 'N', 'Y') notiYn\n" +
                "     , if(c.releasedAt < (select adddate(curdate(),-weekday(curdate())+6)),\n" +
                "                                 (select concat(substr(_UTF8'일월화수목금토',dayofweek(c.releasedAt),1),'요일')),\n" +
                "                                 date_format(c.releasedAt,'%c월%e일')) releasedAt\n" +
                "from CONTENTS_TB c\n" +
                "    left outer join NOTIFICATION_TB n on c.contentId = n.contentId and n.profileId = ?\n" +
                "where c.releasedAt > now()\n";

        int getReleaseParams = profileId;

        return this.jdbcTemplate.query(getReleaseQuery,
                (rs,rowNum)-> new GetReleaseRes(
                        rs.getInt("contentId"),
                        rs.getString("contentTitle"),
                        rs.getString("contentExp"),
                        rs.getString("previewUrl"),
                        rs.getString("notiYn"),
                        rs.getString("releasedAt")
                ),
                getReleaseParams
        );
    }
}
