package com.example.demo.src.signUp;



import com.example.demo.config.BaseException;
import com.example.demo.config.secret.Secret;
import com.example.demo.src.signUp.model.*;
import com.example.demo.src.signUp.model.PatchMembershipReq;
import com.example.demo.src.signUp.model.PatchMembershipRes;
import com.example.demo.utils.AES128;
import com.example.demo.utils.JwtService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import static com.example.demo.config.BaseResponseStatus.*;

// Service Create, Update, Delete 의 로직 처리
@Service
public class SignUpService {
    final Logger logger = LoggerFactory.getLogger(this.getClass());

    private final SignUpDao signUpDao;
    private final SignUpProvider signUpProvider;
    private final JwtService jwtService;


    @Autowired
    public SignUpService(SignUpDao signUpDao, SignUpProvider signUpProvider, JwtService jwtService) {
        this.signUpDao = signUpDao;
        this.signUpProvider = signUpProvider;
        this.jwtService = jwtService;

    }

    //POST
    public PostSignUpRes createUser(PostSignUpReq postSignUpReq) throws BaseException {
        //중복
        if(signUpProvider.checkEmail(postSignUpReq.getEmail()) ==1){
            throw new BaseException(DUPLICATED_EMAIL);
        }

        String pwd;
        try{
            //암호화
            pwd = new AES128(Secret.USER_INFO_PASSWORD_KEY).encrypt(postSignUpReq.getPassword());
            postSignUpReq.setPassword(pwd);
        } catch (Exception ignored) {
            throw new BaseException(PASSWORD_ENCRYPTION_ERROR);
        }
        try{
            int userId = signUpDao.createUser(postSignUpReq);
            //jwt 발급.
            String jwt = jwtService.createJwt(userId);
            return new PostSignUpRes(jwt,userId);
        } catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }

    public PatchMembershipRes modifyMembership(PatchMembershipReq patchMembershipReq) throws BaseException {
        try{
//            int userId = postCreditReq.getUserId();
//            Date membershipStartedAt = signUpDao.createCredit(postCreditReq);
//            return new PostCreditRes(userId, creditId);
            PatchMembershipRes patchMembershipRes = signUpDao.modifyMembership(patchMembershipReq);
            return patchMembershipRes;
        } catch(Exception exception){
            exception.printStackTrace();
            throw new BaseException(DATABASE_ERROR);
        }
    }

    public PostCreditRes createCredit(PostCreditReq postCreditReq) throws BaseException{
        try{
            int userId = postCreditReq.getUserId();
            int creditId = signUpDao.createCredit(postCreditReq);
            return new PostCreditRes(userId, creditId);
        } catch(Exception exception){
            exception.printStackTrace();
            throw new BaseException(DATABASE_ERROR);
        }
    }

}