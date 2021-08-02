package com.example.demo.src.payment;


import com.example.demo.config.BaseException;
import com.example.demo.src.payment.model.GetPaymentDtlRes;
import com.example.demo.src.payment.model.GetPaymentMembershipRes;
import com.example.demo.src.user.model.GetUserDtlRes;
import com.example.demo.utils.JwtService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.example.demo.config.BaseResponseStatus.DATABASE_ERROR;

//Provider : Read의 비즈니스 로직 처리
@Service
public class PaymentProvider {

    private final PaymentDao paymentDao;
    private final JwtService jwtService;


    final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    public PaymentProvider(PaymentDao paymentDao, JwtService jwtService) {
        this.paymentDao = paymentDao;
        this.jwtService = jwtService;
    }

    public List<GetPaymentDtlRes> getPaymentDtl(int userId) throws BaseException {
        try {
            List<GetPaymentDtlRes> getPaymentDtl = paymentDao.getPaymentDtl(userId);
            return getPaymentDtl;
        } catch (Exception exception) {
            exception.printStackTrace();
            throw new BaseException(DATABASE_ERROR);
        }
    }

    public GetPaymentMembershipRes getPaymentMembership(int userId) throws BaseException {
        try {
            GetPaymentMembershipRes getPaymentMembership = paymentDao.getPaymentMembership(userId);
            return getPaymentMembership;
        } catch (Exception exception) {
            exception.printStackTrace();
            throw new BaseException(DATABASE_ERROR);
        }
    }
}
