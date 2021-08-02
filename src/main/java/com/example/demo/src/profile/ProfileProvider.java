package com.example.demo.src.profile;


import com.example.demo.config.BaseException;
import com.example.demo.config.secret.Secret;
import com.example.demo.src.login.model.PostLoginReq;
import com.example.demo.src.login.model.PostLoginRes;
import com.example.demo.src.payment.model.GetPaymentDtlRes;
import com.example.demo.src.profile.model.GetProfileRes;
import com.example.demo.src.profile.model.PostProfileLoginReq;
import com.example.demo.src.profile.model.PostProfileLoginRes;
import com.example.demo.src.profile.model.Profile;
import com.example.demo.src.user.model.User;
import com.example.demo.utils.AES128;
import com.example.demo.utils.JwtService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.example.demo.config.BaseResponseStatus.*;

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

    public List<GetProfileRes> getProfile(int userId) throws BaseException {
        try {
            List<GetProfileRes> getProfile = profileDao.getProfile(userId);
            return getProfile;
        } catch (Exception exception) {
            exception.printStackTrace();
            throw new BaseException(DATABASE_ERROR);
        }
    }

    public String checkLock(int profileId) throws BaseException {
        try {
            String checkLock = profileDao.checkLock(profileId);
            return checkLock;
        } catch (Exception exception) {
            exception.printStackTrace();
            throw new BaseException(DATABASE_ERROR);
        }
    }

    public PostProfileLoginRes logIn(PostProfileLoginReq postProfileLoginReq) throws BaseException{
        try{
            //삭제된 프로필
            if(checkProfileStatus(postProfileLoginReq.getProfileId()).equals("D")){
                throw new BaseException(DELETE_STATUS_PROFILE);
            }
        }catch(Exception e){
            throw new BaseException(NO_EXIST_PROFILE);
        }

        if(postProfileLoginReq.getIsLock().equals("Y")){
            Profile profile = profileDao.getPin(postProfileLoginReq);
//        User user = loginDao.getPwd(postLoginReq);
            String pin;
            try {
                pin = new AES128(Secret.USER_INFO_PASSWORD_KEY).decrypt(profile.getPin());
            } catch (Exception ignored) {
                throw new BaseException(PIN_DECRYPTION_ERROR);
            }

            if(!postProfileLoginReq.getPin().equals(pin)){
                throw new BaseException(FAILED_TO_PROFILE_LOGIN);
            }
        }

        int profileId = postProfileLoginReq.getProfileId();
        String profileJwt = jwtService.createProfileJwt(profileId);
        return new PostProfileLoginRes(profileId,profileJwt);

    }

    private String checkProfileStatus(int profileId) throws BaseException {
        try{
            String checkProfileStatus = profileDao.checkProfileStatus(profileId);
            return checkProfileStatus;
        }catch(Exception e){
            throw new BaseException(DATABASE_ERROR);
        }
    }

}
