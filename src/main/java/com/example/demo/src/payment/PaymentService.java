package com.example.demo.src.payment;


import com.example.demo.config.BaseException;
import com.example.demo.src.payment.model.PatchCreditReq;
import com.example.demo.utils.JwtService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import static com.example.demo.config.BaseResponseStatus.*;

// Service Create, Update, Delete 의 로직 처리
@Service
public class PaymentService {
    final Logger logger = LoggerFactory.getLogger(this.getClass());

    private final PaymentDao paymentDao;
    private final PaymentProvider paymentProvider;
    private final JwtService jwtService;


    @Autowired
    public PaymentService(PaymentDao paymentDao, PaymentProvider paymentProvider, JwtService jwtService) {
        this.paymentDao = paymentDao;
        this.paymentProvider = paymentProvider;
        this.jwtService = jwtService;

    }
    public void modifyCredit(PatchCreditReq patchCreditReq) throws BaseException{
        try{
            int result = paymentDao.modifyCredit(patchCreditReq);
            if(result == 0){
                throw new BaseException(MODIFY_FAIL_CREDIT);
            }
        } catch(Exception exception){
            exception.printStackTrace();
            throw new BaseException(DATABASE_ERROR);
        }
    }
}
