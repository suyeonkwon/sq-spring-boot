package com.example.demo.src.login;


import com.example.demo.src.login.model.PostLoginReq;
import com.example.demo.src.user.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.List;

@Repository
public class LoginDao {

    private JdbcTemplate jdbcTemplate;

    @Autowired
    public void setDataSource(DataSource dataSource){
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    public int checkEmail(String email){
        String checkEmailQuery = "select exists(select email from USER_TB where email = ?)";
        String checkEmailParams = email;
        return this.jdbcTemplate.queryForObject(checkEmailQuery,
                int.class,
                checkEmailParams);

    }

    public User getPwd(PostLoginReq postLoginReq){
        String getPwdQuery = "select userId, password,email from USER_TB where email = ?";
        String getPwdParams = postLoginReq.getEmail();

        return this.jdbcTemplate.queryForObject(getPwdQuery,
                (rs,rowNum)-> new User(
                        rs.getInt("userId"),
                        rs.getString("password"),
                        rs.getString("email")
                ),
                getPwdParams
                );

    }

    public User getPwd(PatchPasswordReq patchPasswordReq){
        String getPwdQuery = "select userId, password, email from USER_TB where userId = ?";
        int getPwdParams = patchPasswordReq.getUserId();

        return this.jdbcTemplate.queryForObject(getPwdQuery,
                (rs,rowNum)-> new User(
                        rs.getInt("userId"),
                        rs.getString("password"),
                        rs.getString("email")
                ),
                getPwdParams
        );
    }

    public String checkUserStatus(String email) {
        String checkUserStatusQuery = "select status from USER_TB where email = ?";
        String checkEmailParams = email;
        return this.jdbcTemplate.queryForObject(checkUserStatusQuery,
                String.class,
                checkEmailParams);
    }

}
