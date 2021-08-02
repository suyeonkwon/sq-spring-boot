package com.example.demo.src.setting;


import com.example.demo.src.profile.model.*;
import com.example.demo.src.setting.model.GetRatedRes;
import com.example.demo.src.setting.model.GetViewedRes;
import com.example.demo.src.setting.model.PatchRatedReq;
import com.example.demo.src.setting.model.PatchViewedHidReq;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.List;

@Repository
public class SettingDao {

    private JdbcTemplate jdbcTemplate;

    @Autowired
    public void setDataSource(DataSource dataSource){
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    public String checkLock(int profileId) {
        String checkLockQuery = "select isLock from PROFILE_TB where profileId = ?";
        int getProfileParams = profileId;
        return this.jdbcTemplate.queryForObject(checkLockQuery,String.class,getProfileParams);
    }

    public Profile getPin(PostProfileLoginReq postProfileLoginReq) {
        String getPinQuery = "select userId, profileId, pin, isLock from PROFILE_TB where profileId = ?";
        int getPinParams = postProfileLoginReq.getProfileId();

        return this.jdbcTemplate.queryForObject(getPinQuery,
                (rs,rowNum)-> new Profile(
                        rs.getInt("userId"),
                        rs.getInt("profileId"),
                        rs.getString("pin"),
                        rs.getString("isLock")
                ),
                getPinParams
        );
    }

    public int modifyLock(PatchProfileLockReq patchProfileLockReq) {
        String modifyLockQuery = "update PROFILE_TB set pin = ? , isLock = 'Y' where profileId = ? ";
        Object[] modifyLockParams = new Object[]{patchProfileLockReq.getPin(),patchProfileLockReq.getProfileId()};

        return this.jdbcTemplate.update(modifyLockQuery,modifyLockParams);

    }

    public int modifyUnLock(PatchProfileLockReq patchProfileLockReq) {
        String modifyLockQuery = "update PROFILE_TB set pin = null, isLock = 'N' where profileId = ? ";
        int modifyLockParams = patchProfileLockReq.getProfileId();

        return this.jdbcTemplate.update(modifyLockQuery,modifyLockParams);

    }

    public List<GetViewedRes> getWatched(int profileId) {
        String getViewedQuery = "select PT.profileId\n" +
                "     , PT.profileNm\n" +
                "     , PT.profileImgUrl\n" +
                "     , WRT.recodeId\n" +
                "     , ET.episodeId\n" +
                "     , date_format(WRT.watchedAt, '%y.%m.%e.') watchedAt\n" +
                "     , concat(CT.contentTitle,': 시즌',ET.season,':\"',ET.episodeNo,'화\"') title\n" +
                "     , WRT.isHidden\n" +
                "     , WRT.status\n" +
                "from WATCH_RECODE_TB WRT\n" +
                "    left outer join PROFILE_TB PT on WRT.profileId = PT.profileId\n" +
                "    left outer join EPISODE_TB ET on WRT.episodeId = ET.episodeId\n" +
                "    left outer join CONTENTS_TB CT on ET.contentId = CT.contentId\n" +
                "where WRT.profileId = ? and\n" +
                "      WRT.isHidden = 'N' and\n" +
                "      WRT.status = 'A';\n";
        int getViewedParams = profileId;

        return this.jdbcTemplate.query(getViewedQuery,
                (rs,rowNum)-> new GetViewedRes(
                        rs.getInt("profileId"),
                        rs.getString("profileNm"),
                        rs.getString("profileImgUrl"),
                        rs.getInt("recodeId"),
                        rs.getInt("episodeId"),
                        rs.getString("watchedAt"),
                        rs.getString("title")
                ),
                getViewedParams
        );

    }

    public int modifyViewedHid(PatchViewedHidReq patchViewedHidReq) {
        String modifyViewedHidQuery = "update WATCH_RECODE_TB set status = 'D' where recodeId = ? ";
        int modifyViewedHidParams = patchViewedHidReq.getRecodeId();

        return this.jdbcTemplate.update(modifyViewedHidQuery,modifyViewedHidParams);

    }

    public List<GetRatedRes> getRated(int profileId) {
        String getRatedQuery = "select p.profileId\n" +
                "     , p.profileNm\n" +
                "     , p.profileImgUrl\n" +
                "     , r.rateId\n" +
                "     , date_format(r.createdAt,'%y.%c.%e.') createdAt\n" +
                "     , c.contentId\n" +
                "     , c.contentTitle\n" +
                "     , r.rateGb\n" +
                "from RATE_TB r\n" +
                "    left outer join PROFILE_TB p on r.profileId = p.profileId\n" +
                "    left outer join CONTENTS_TB c on r.contentId = c.contentId\n" +
                "where p.profileId = ? and\n" +
                "      r.status = 'A'";
        int getRatedParams = profileId;

        return this.jdbcTemplate.query(getRatedQuery,
                (rs,rowNum)-> new GetRatedRes(
                        rs.getInt("profileId"),
                        rs.getString("profileNm"),
                        rs.getString("profileImgUrl"),
                        rs.getInt("rateId"),
                        rs.getString("createdAt"),
                        rs.getInt("contentId"),
                        rs.getString("contentTitle"),
                        rs.getString("rateGb")
                ),
                getRatedParams
        );
    }

    public int modifyRatedStatus(PatchRatedReq patchRatedReq) {
        String modifyRatedStatusQuery = "update RATE_TB set status = 'D' where profileId = ? and contentId = ? and status='A'";
        Object[] modifyRatedStatusParams = new Object[]{patchRatedReq.getProfileId(), patchRatedReq.getContentId()};

        return this.jdbcTemplate.update(modifyRatedStatusQuery,modifyRatedStatusParams);
    }

    public int modifyRated(PatchRatedReq patchRatedReq) {
        String modifyRatedQuery = "insert into RATE_TB(rateId,contentId, profileId, rateGb)\n" +
                "    (\n" +
                "        select if(count(*) = 0, null, rateId)\n" +
                "             , ?\n" +
                "             , ?\n" +
                "             , ?\n" +
                "         from RATE_TB\n" +
                "         where contentId = ?\n" +
                "           and profileId = ?\n" +
                "           and status = 'A'\n" +
                "\n" +
                "    )\n" +
                "on duplicate key update\n" +
                "rateGb = ?";
        Object[] modifyRatedParams = new Object[]{patchRatedReq.getContentId(), patchRatedReq.getProfileId(),
                                                        patchRatedReq.getRateGb(), patchRatedReq.getContentId(),
                                                        patchRatedReq.getProfileId(), patchRatedReq.getRateGb()};

        return this.jdbcTemplate.update(modifyRatedQuery,modifyRatedParams);
    }
}
