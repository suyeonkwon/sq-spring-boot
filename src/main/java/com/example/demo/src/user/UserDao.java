package com.example.demo.src.user;


import com.example.demo.src.login.model.PostLoginReq;
import com.example.demo.src.user.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.List;

@Repository
public class UserDao {

    private JdbcTemplate jdbcTemplate;

    @Autowired
    public void setDataSource(DataSource dataSource){
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    public List<GetUserRes> getUsers(){
        String getUsersQuery = "SELECT u.userId\n" +
                "     , u.email\n" +
                "     , u.membershipCd\n" +
                "     , u.phoneNumber\n" +
                "     , c.commDtlNm membershipNm\n" +
                "     , u.password\n" +
                "     , u.membershipStartedAt\n" +
                "     , u.isMemberShipUsed\n" +
                "from USER_TB u left outer join comm_dtl_tb c on u.membershipCd = c.commDtlCd\n" +
                "where c.commCd = 'membership'";
        return this.jdbcTemplate.query(getUsersQuery,
                (rs,rowNum) -> new GetUserRes(
                        rs.getInt("userId"),
                        rs.getString("email"),
                        rs.getString("phoneNumber"),
                        rs.getString("membershipCd"),
                        rs.getString("membershipNm"),
                        rs.getString("password"),
                        rs.getDate("membershipStartedAt"),
                        rs.getString("isMemberShipUsed"))
                );
    }

        public GetUserDtlRes getUser(int userId){
        String getUserQuery = "SELECT u.userId\n" +
                "     , u.email\n" +
                "     , u.phoneNumber\n" +
                "     , u.membershipCd\n" +
                "     , c.commDtlNm membershipNm\n" +
                "     , u.password\n" +
                "     , date_format(u.membershipStartedAt,'%m월%Y') membershipStartedAt\n" +
                "     , u.isMemberShipUsed\n" +
                "     , date_format(date_sub(date_add(MAX(p.paymentedAt),interval 1 month),INTERVAL 1 day),'%Y년%m월%d일') nextPaymentedAt\n" +
                "     , replace(ct.creditNo,substr(ct.creditNo,1,12),'************') creditNo\n" +
                "from USER_TB u\n" +
                "    left outer join COMM_DTL_TB c\n" +
                "        on u.membershipCd = c.commDtlCd\n" +
                "    left outer join PAYMENT_TB p\n" +
                "        ON u.userId = p.userId\n" +
                "    LEFT OUTER JOIN CREDITCARD_TB ct\n" +
                "        on u.userId = ct.userId\n" +
                "where c.commCd = 'membership' AND\n" +
                "      u.userId = ?";
        int getUserParams = userId;
        return this.jdbcTemplate.queryForObject(getUserQuery,
                (rs, rowNum) -> new GetUserDtlRes(
                        rs.getInt("userId"),
                        rs.getString("email"),
                        rs.getString("phoneNumber"),
                        rs.getString("membershipCd"),
                        rs.getString("membershipNm"),
                        rs.getString("password"),
                        rs.getString("membershipStartedAt"),
                        rs.getString("isMemberShipUsed"),
                        rs.getString("nextPaymentedAt"),
                        rs.getString("creditNo")),
                getUserParams);
    }

    public int createUser(PostUserReq postUserReq){
//        String createUserQuery = "insert into UserInfo (userName, ID, password, email) VALUES (?,?,?,?)";
        String createUserQuery = "INSERT INTO netflix.USER_TB (membershipCd, password, membershipStartedAt, phoneNumber, isMemberShipUsed, createdAt, updatedAt, status, email) VALUES (null, ?, null, null, DEFAULT, DEFAULT, DEFAULT, DEFAULT, ?)";
        Object[] createUserParams = new Object[]{postUserReq.getPassword(),postUserReq.getEmail()};

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

    public int modifyUserName(PatchUserReq patchUserReq){
        String modifyUserNameQuery = "update UserInfo set userName = ? where userIdx = ? ";
        Object[] modifyUserNameParams = new Object[]{patchUserReq.getUserName(), patchUserReq.getUserIdx()};

        return this.jdbcTemplate.update(modifyUserNameQuery,modifyUserNameParams);
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

    public int modifyMembership(PatchMembershipReq patchMembershipReq) {
        String modifyMembershipQuery = "update USER_TB set membershipCd = ? where userId = ? ";
        Object[] modifyMembershipParams = new Object[]{patchMembershipReq.getMembershipCd(), patchMembershipReq.getUserId()};

        return this.jdbcTemplate.update(modifyMembershipQuery,modifyMembershipParams);
    }

    public int modifyEmail(PatchEmailReq patchEmailReq) {
        String modifyEmailQuery = "update USER_TB set email = ? where userId = ? ";
        Object[] modifyEmailQueryParams = new Object[]{patchEmailReq.getEmail(), patchEmailReq.getUserId()};

        return this.jdbcTemplate.update(modifyEmailQuery,modifyEmailQueryParams);
    }

    public int modifyPassword(PatchPasswordReq patchPasswordReq) {
        String modifyPasswordQuery = "update USER_TB set password = ? where userId = ? ";
        Object[] modifyPasswordQueryParams = new Object[]{patchPasswordReq.getNewPassword(), patchPasswordReq.getUserId()};

        return this.jdbcTemplate.update(modifyPasswordQuery,modifyPasswordQueryParams);
    }

    public int modifyPhone(PatchPhoneReq patchPhoneReq) {
        String modifyPhoneQuery = "update USER_TB set phoneNumber = ? where userId = ? ";
        Object[] modifyPhoneQueryParams = new Object[]{patchPhoneReq.getPhoneNumber(), patchPhoneReq.getUserId()};

        return this.jdbcTemplate.update(modifyPhoneQuery,modifyPhoneQueryParams);
    }

    public int checkMembership(String membershipCd) {
        String checkMembershipQuery = "select exists(\n" +
                "    select commDtlCd from COMM_DTL_TB where commCd = 'membership' and\n" +
                "                                            status = 'A' and\n" +
                "                                            commDtlCd = ? \n" +
                "           )";
        String checkMembershipParams = membershipCd;
        return this.jdbcTemplate.queryForObject(checkMembershipQuery,
                int.class,
                checkMembershipParams);
    }
}
