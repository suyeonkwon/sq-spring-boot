package com.example.demo.src.signUp;


import com.example.demo.src.signUp.model.PatchMembershipReq;
import com.example.demo.src.signUp.model.PatchMembershipRes;
import com.example.demo.src.signUp.model.PostCreditReq;
import com.example.demo.src.signUp.model.PostSignUpReq;
import com.example.demo.src.user.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.List;

@Repository
public class SignUpDao {

    private JdbcTemplate jdbcTemplate;

    @Autowired
    public void setDataSource(DataSource dataSource){
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    public int createUser(PostSignUpReq postSignUpReq){
//        String createUserQuery = "insert into UserInfo (userName, ID, password, email) VALUES (?,?,?,?)";
        String createUserQuery = "INSERT INTO netflix.USER_TB (membershipCd, password, membershipStartedAt, phoneNumber, isMemberShipUsed, createdAt, updatedAt, status, email) VALUES (null, ?, null, null, DEFAULT, DEFAULT, DEFAULT, DEFAULT, ?)";
        Object[] createUserParams = new Object[]{postSignUpReq.getPassword(),postSignUpReq.getEmail()};

        this.jdbcTemplate.update(createUserQuery, createUserParams);

        String lastInserIdQuery = "select last_insert_id()";
        return this.jdbcTemplate.queryForObject(lastInserIdQuery,int.class);
    }

    public int checkEmail(String email){
        String checkEmailQuery = "select exists(select email from USER_TB where email = ?)";
        String checkEmailParams = email;
        return this.jdbcTemplate.queryForObject(checkEmailQuery,
                int.class,
                checkEmailParams);

    }

    public PatchMembershipRes modifyMembership(PatchMembershipReq patchMembershipReq) {
        String modifyMembershipQuery = "update USER_TB set membershipCd = ? , membershipStartedAt = current_timestamp(), isMemberShipUsed = 'Y' where userId = ? ";
        Object[] modifyMembershipParams = new Object[]{patchMembershipReq.getMembershipCd(), patchMembershipReq.getUserId()};

        this.jdbcTemplate.update(modifyMembershipQuery,modifyMembershipParams);

        String membershipInfoQuery = "select userId, membershipCd,membershipStartedAt from USER_TB where userId = ?";
        int membershipInfoParmas = patchMembershipReq.getUserId();

        return this.jdbcTemplate.queryForObject(membershipInfoQuery,
                (rs,rowNum)-> new PatchMembershipRes(
                        rs.getInt("userId"),
                        rs.getString("membershipCd"),
                        rs.getDate("membershipStartedAt")
                ),
                membershipInfoParmas
        );

    }

    public int createCredit(PostCreditReq postCreditReq) {
        String createCreditQuery = "INSERT INTO netflix.CREDITCARD_TB (userId, creditNo, expiryDt, holderNm, holderYear, holderMonth, holderDay, createdAt, updatedAt, status) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, DEFAULT, DEFAULT, DEFAULT)";

        Object[] createCreditQueryParams = new Object[]{postCreditReq.getUserId(),postCreditReq.getCreditNo(), postCreditReq.getExpiryDt(),
                postCreditReq.getHolderNm(), postCreditReq.getHolderYear(),
                postCreditReq.getHolderMonth(), postCreditReq.getHolderDay()};

        this.jdbcTemplate.update(createCreditQuery, createCreditQueryParams);

        String lastInserIdQuery = "select last_insert_id()";
        return this.jdbcTemplate.queryForObject(lastInserIdQuery,int.class);
    }
}
