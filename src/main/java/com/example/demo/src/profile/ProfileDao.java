package com.example.demo.src.profile;


import com.example.demo.src.payment.model.GetPaymentDtlRes;
import com.example.demo.src.payment.model.PatchCreditReq;
import com.example.demo.src.profile.model.GetProfileRes;
import com.example.demo.src.profile.model.PostProfileReq;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.List;

@Repository
public class ProfileDao {

    private JdbcTemplate jdbcTemplate;

    @Autowired
    public void setDataSource(DataSource dataSource){
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    public List<GetProfileRes> getProfile(int userId) {
        String getProfileQuery = "select userId, profileId, profileNm, profileImgUrl from PROFILE_TB" +
                " WHERE userId = ?";
        int getProfileParams = userId;
        return this.jdbcTemplate.query(getProfileQuery,
                (rs, rowNum) -> new GetProfileRes(
                        rs.getInt("userId"),
                        rs.getInt("profileId"),
                        rs.getString("profileNm"),
                        rs.getString("profileImgUrl")),
                        getProfileParams);
    }

    public int createProfile(PostProfileReq postProfileReq) {
        String createProfileQuery = "INSERT INTO netflix.PROFILE_TB (userId, profileNm, profileImgUrl, language, watchLimit, isKid, pin, createdAt, updatedAt, status) " +
                "VALUES (?, ?, null, DEFAULT, ?, ?, null, DEFAULT, DEFAULT, DEFAULT)";
        Object[] createProfileParams = new Object[]{postProfileReq.getUserId(),postProfileReq.getProfileNm(),
                                                 postProfileReq.getWatchLimit(),postProfileReq.getIsKid()};

        this.jdbcTemplate.update(createProfileQuery, createProfileParams);

        String lastInserIdQuery = "select last_insert_id()";
        return this.jdbcTemplate.queryForObject(lastInserIdQuery,int.class);
    }
}
