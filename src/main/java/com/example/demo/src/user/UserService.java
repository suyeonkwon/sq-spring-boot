package com.example.demo.src.user;



import com.example.demo.config.BaseException;
import com.example.demo.config.secret.Secret;
import com.example.demo.src.user.model.*;
import com.example.demo.utils.AES128;
import com.example.demo.utils.JwtService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import static com.example.demo.config.BaseResponseStatus.*;

// Service Create, Update, Delete 의 로직 처리
@Service
public class UserService {
    final Logger logger = LoggerFactory.getLogger(this.getClass());

    private final UserDao userDao;
    private final UserProvider userProvider;
    private final JwtService jwtService;


    @Autowired
    public UserService(UserDao userDao, UserProvider userProvider, JwtService jwtService) {
        this.userDao = userDao;
        this.userProvider = userProvider;
        this.jwtService = jwtService;

    }

    //POST
    public PostUserRes createUser(PostUserReq postUserReq) throws BaseException {
        //중복
        if(userProvider.checkEmail(postUserReq.getEmail()) ==1){
            throw new BaseException(DUPLICATED_EMAIL);
        }

        String pwd;
        try{
            //암호화
            pwd = new AES128(Secret.USER_INFO_PASSWORD_KEY).encrypt(postUserReq.getPassword());
            postUserReq.setPassword(pwd);
        } catch (Exception ignored) {
            throw new BaseException(PASSWORD_ENCRYPTION_ERROR);
        }
        try{
            int userId = userDao.createUser(postUserReq);
            //jwt 발급.
            String jwt = jwtService.createJwt(userId);
            return new PostUserRes(jwt,userId);
        } catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }

    public void modifyUserName(PatchUserReq patchUserReq) throws BaseException {
        try{
            int result = userDao.modifyUserName(patchUserReq);
            if(result == 0){
                throw new BaseException(MODIFY_FAIL_USERNAME);
            }
        } catch(Exception exception){
            throw new BaseException(DATABASE_ERROR);
        }
    }

    public void modifyMembership(PatchMembershipReq patchMembershipReq) throws BaseException {
        try{
            int result = userDao.modifyMembership(patchMembershipReq);
            if(result == 0){
                throw new BaseException(MODIFY_FAIL_MEMBERSHIP);
            }
        } catch(Exception exception){
            throw new BaseException(DATABASE_ERROR);
        }
    }

    public void modifyEmail(PatchEmailReq patchEmailReq) throws BaseException{
        //중복
        if(userProvider.checkEmail(patchEmailReq.getEmail()) ==1){
            throw new BaseException(DUPLICATED_EMAIL);
        }

        try{
            int result = userDao.modifyEmail(patchEmailReq);
            if(result == 0){
                throw new BaseException(MODIFY_FAIL_EMAIL);
            }
        } catch(Exception exception){
            exception.printStackTrace();
            throw new BaseException(DATABASE_ERROR);
        }
    }

    public void modifyPassword(PatchPasswordReq patchPasswordReq) throws BaseException {
        User user = userDao.getPwd(patchPasswordReq);
        String password;
        try {
            password = new AES128(Secret.USER_INFO_PASSWORD_KEY).decrypt(user.getPassword());
        } catch (Exception ignored) {
            throw new BaseException(PASSWORD_DECRYPTION_ERROR);
        }

        if(patchPasswordReq.getPassword().equals(password)){
            String pwd;
            try{
                //암호화
                pwd = new AES128(Secret.USER_INFO_PASSWORD_KEY).encrypt(patchPasswordReq.getNewPassword());
                patchPasswordReq.setNewPassword(pwd);
            } catch (Exception ignored) {
                throw new BaseException(PASSWORD_ENCRYPTION_ERROR);
            }
            try{
                int result = userDao.modifyPassword(patchPasswordReq);
                if(result == 0){
                    throw new BaseException(MODIFY_FAIL_PASSWORD);
                }
            } catch(Exception exception){
                exception.printStackTrace();
                throw new BaseException(DATABASE_ERROR);
            }
        }
        else{
            throw new BaseException(PASSWORD_NO_EQUALS_ERROR);
        }
    }

    public void modifyPhone(PatchPhoneReq patchPhoneReq) throws BaseException {
        try{
            int result = userDao.modifyPhone(patchPhoneReq);
            if(result == 0){
                throw new BaseException(MODIFY_FAIL_PHONE_NUMBER);
            }
        } catch(Exception exception){
            exception.printStackTrace();
            throw new BaseException(DATABASE_ERROR);
        }
    }
}
