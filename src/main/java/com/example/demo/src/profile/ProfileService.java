package com.example.demo.src.profile;


import com.example.demo.config.BaseException;
import com.example.demo.config.secret.Secret;
import com.example.demo.src.payment.model.PatchCreditReq;
import com.example.demo.src.profile.model.PatchProfileLockReq;
import com.example.demo.src.profile.model.PostProfileReq;
import com.example.demo.src.profile.model.PostProfileRes;
import com.example.demo.src.signUp.model.PostSignUpRes;
import com.example.demo.src.user.model.User;
import com.example.demo.utils.AES128;
import com.example.demo.utils.JwtService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import static com.example.demo.config.BaseResponseStatus.*;
import static com.example.demo.config.BaseResponseStatus.PASSWORD_ENCRYPTION_ERROR;

// Service Create, Update, Delete 의 로직 처리
@Service
public class ProfileService {
    final Logger logger = LoggerFactory.getLogger(this.getClass());

    private final ProfileDao profileDao;
    private final ProfileProvider profileProvider;
    private final JwtService jwtService;


    @Autowired
    public ProfileService(ProfileDao profileDao, ProfileProvider profileProvider, JwtService jwtService) {
        this.profileDao = profileDao;
        this.profileProvider = profileProvider;
        this.jwtService = jwtService;

    }

    public PostProfileRes createProfile(PostProfileReq postProfileReq) throws BaseException {
        //키즈계정 확인
        if(postProfileReq.getIsKid() == "Y"){
            postProfileReq.setWatchLimit("12");
        }else{
            postProfileReq.setIsKid("N");
            postProfileReq.setWatchLimit("19");
        }
        try{
            int userId = postProfileReq.getUserId();
            int profileId = profileDao.createProfile(postProfileReq);
            //jwt 발급.
//            String jwt = jwtService.createJwt(profileId);
            return new PostProfileRes(userId,profileId);
        } catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }
}
