package com.example.demo.src.payment;


import com.example.demo.src.payment.model.GetPaymentDtlRes;
import com.example.demo.src.payment.model.GetPaymentMembershipRes;
import com.example.demo.src.payment.model.PatchCreditReq;
import com.example.demo.src.user.model.GetUserDtlRes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.List;

@Repository
public class PaymentDao {

    private JdbcTemplate jdbcTemplate;

    @Autowired
    public void setDataSource(DataSource dataSource){
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    public List<GetPaymentDtlRes> getPaymentDtl(int userId) {
        String getPaymentDtQuery = "select p.userId" +
                "     , date_format(p.paymentedAt,'%Y년%m일%d일') paymentedAt\n" +
                "     , date_format(date_sub(date_add(p.paymentedAt,interval  1 month), interval  1 day ), '%Y년%m월%d일') nextMonth\n" +
                "     , p.remark\n" +
                "     , format(p.price,0) price\n" +
                "     , format(p.price*0.1,0) vat\n" +
                "     , format(p.totalPrice,0) totalPrice\n" +
                "     , replace(p.creditNo, substr(p.creditNo,1,12),'************') creditNo\n" +
                "from PAYMENT_TB p\n" +
                "    left outer join USER_TB u on p.userId = u.userId\n" +
                "where p.userId = ?\n" +
                "order by p.paymentedAt desc";
        int getPaymentDtParams = userId;
        return this.jdbcTemplate.query(getPaymentDtQuery,
                (rs, rowNum) -> new GetPaymentDtlRes(
                        rs.getInt("userId"),
                        rs.getString("paymentedAt"),
                        rs.getString("nextMonth"),
                        rs.getString("remark"),
                        rs.getString("price"),
                        rs.getString("vat"),
                        rs.getString("totalPrice"),
                        rs.getString("creditNo")),
                getPaymentDtParams);
    }

    public int modifyCredit(PatchCreditReq patchCreditReq) {
        String modifyCreditQuery = "update CREDITCARD_TB set " +
                "creditNo = ? , expiryDt = ? , holderNm = ?  , holderYear = ? , holderMonth = ? , holderDay = ?" +
                "where userId = ? ";
        Object[] modifyCreditQueryParams = new Object[]{patchCreditReq.getCreditNo(), patchCreditReq.getExpiryDt(),
                patchCreditReq.getHolderNm(), patchCreditReq.getHolderYear(),
                patchCreditReq.getHolderMonth(), patchCreditReq.getHolderDay(),
                patchCreditReq.getUserId()};

        return this.jdbcTemplate.update(modifyCreditQuery,modifyCreditQueryParams);
    }

    public GetPaymentMembershipRes getPaymentMembership(int userId) {
        String getPaymentMembershipQuery = "select a.userId\n" +
                "     ,  concat(a.membership,':월',a.price,'원') membership\n" +
                "     , concat('동시접속',a.simultanConn,'명') simultanConn\n" +
                "     , max(a.nextMonth) nextMonth\n" +
                "from (\n" +
                "     select u.userId\n" +
                "          , date_format(date_sub(date_add(p.paymentedAt,INTERVAL 1 MONTH),INTERVAL 1 DAY),'%Y년%m월%d일') nextMonth\n" +
                "          , cm.commDtlNm membership\n" +
                "          , format(m.price,0) price\n" +
                "          , m.simultanConn\n" +
                "    from PAYMENT_TB p\n" +
                "        left outer join USER_TB u on p.userId = u.userId\n" +
                "        left outer join MEMBERSHIP_TB m on u.membershipCd = m.membershipCd\n" +
                "        left outer join COMM_DTL_TB cm on cm.commDtlCd = u.membershipCd and cm.commCd = 'membership'\n" +
                "    where p.userId = ? and\n" +
                "          u.isMemberShipUsed = 'Y'\n" +
                "         )a";
        int getPaymentMembershipParams = userId;
        return this.jdbcTemplate.queryForObject(getPaymentMembershipQuery,
                (rs, rowNum) -> new GetPaymentMembershipRes(
                        rs.getInt("userId"),
                        rs.getString("membership"),
                        rs.getString("simultanConn"),
                        rs.getString("nextMonth")),
                getPaymentMembershipParams);
    }
}
