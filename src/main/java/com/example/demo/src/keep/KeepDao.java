package com.example.demo.src.keep;

import com.example.demo.src.keep.model.GetKeepRes;
import com.example.demo.src.keep.model.PatchKeepReq;
import com.example.demo.src.keep.model.PostKeepReq;
import com.example.demo.src.setting.model.GetViewedRes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.List;

@Repository
public class KeepDao {
    private JdbcTemplate jdbcTemplate;

    @Autowired
    public void setDataSource(DataSource dataSource){
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    public List<GetKeepRes> getKeep(int profileId) {
        String getKeepQuery = "select p.profileId\n" +
                "     , k.keepId\n" +
                "     , c.contentId\n" +
                "     , c.contentTitle\n" +
                "     , c.contentImgUrl\n" +
                "     , k.status\n" +
                "from KEEP_TB k\n" +
                "    left outer join PROFILE_TB p ON k.profileId = p.profileId\n" +
                "    left outer join CONTENTS_TB c on k.contentId = c.contentId\n" +
                "where p.profileId = ? and\n" +
                "      k.status = 'A'\n" +
                "order by k.createdAt desc";
        int getKeepParams = profileId;

        return this.jdbcTemplate.query(getKeepQuery,
                (rs,rowNum)-> new GetKeepRes(
                        rs.getInt("profileId"),
                        rs.getInt("keepId"),
                        rs.getInt("contentId"),
                        rs.getString("contentTitle"),
                        rs.getString("contentImgUrl")
                ),
                getKeepParams
        );
    }

    public int createKeep(PostKeepReq postKeepReq) {
        String createKeepQuery = "INSERT INTO netflix.KEEP_TB (contentId, profileId, createdAt, updatedAt, status) VALUES (?, ?, DEFAULT, DEFAULT, DEFAULT)";
        Object[] createKeepParams = new Object[]{postKeepReq.getContentId(),postKeepReq.getProfileId()};

        this.jdbcTemplate.update(createKeepQuery, createKeepParams);

        String lastInserIdQuery = "select last_insert_id()";
        return this.jdbcTemplate.queryForObject(lastInserIdQuery,int.class);
    }

    public int modifyKeep(PatchKeepReq patchKeepReq) {
        String createKeepQuery = "update netflix.KEEP_TB set status = 'D' where contentId = ? and profileId = ? and status = 'A'";
        Object[] createKeepParams = new Object[]{patchKeepReq.getContentId(),patchKeepReq.getProfileId()};

        return this.jdbcTemplate.update(createKeepQuery, createKeepParams);
    }
}
