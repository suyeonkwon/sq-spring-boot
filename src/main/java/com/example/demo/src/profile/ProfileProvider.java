package com.example.demo.src.profile;


import com.example.demo.config.BaseException;
import com.example.demo.src.payment.model.GetPaymentDtlRes;
import com.example.demo.src.profile.model.GetProfileRes;
import com.example.demo.utils.JwtService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.example.demo.config.BaseResponseStatus.DATABASE_ERROR;

//Provider : Read의 비즈니스 로직 처리
@Service
public class ProfileProvider {

    private final ProfileDao profileDao;
    private final JwtService jwtService;


    final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    public ProfileProvider(ProfileDao profileDao, JwtService jwtService) {
        this.profileDao = profileDao;
        this.jwtService = jwtService;
    }

//    public List<GetPaymentDtlRes> getPaymentDtl(int userId) throws BaseException {
//        try {
//            List<GetPaymentDtlRes> getPaymentDtl = paymentDao.getPaymentDtl(userId);
//            return getPaymentDtl;
//        } catch (Exception exception) {
//            exception.printStackTrace();
//            throw new BaseException(DATABASE_ERROR);
//        }
//    }

    public List<GetProfileRes> getProfile(int userId) throws BaseException {
        try {
            List<GetProfileRes> getProfile = profileDao.getProfile(userId);
            return getProfile;
        } catch (Exception exception) {
            exception.printStackTrace();
            throw new BaseException(DATABASE_ERROR);
        }
    }
}
