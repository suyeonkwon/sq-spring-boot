package com.example.demo.src.signUp;

import com.example.demo.config.BaseException;
import com.example.demo.config.BaseResponse;
import com.example.demo.src.signUp.SignUpProvider;
import com.example.demo.src.signUp.SignUpService;
import com.example.demo.src.signUp.model.*;
import com.example.demo.utils.JwtService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import static com.example.demo.config.BaseResponseStatus.*;

@RestController
@RequestMapping("/app/signUp")
public class SignUpController {
    final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private final SignUpProvider signUpProvider;
    @Autowired
    private final SignUpService signUpService;
    @Autowired
    private final JwtService jwtService;




    public SignUpController(SignUpProvider signUpProvider, SignUpService signUpService, JwtService jwtService){
        this.signUpProvider = signUpProvider;
        this.signUpService = signUpService;
        this.jwtService = jwtService;
    }

    /**
     * 회원가입 API
     * [POST] /signUp
     * @return BaseResponse<PostSignUpRes>
     */
    // Body
    @ResponseBody
    @PostMapping("")
    public BaseResponse<PostSignUpRes> createUser(@RequestBody PostSignUpReq postSignUpReq) {
        // TODO: email 관련한 짧은 validation 예시입니다. 그 외 더 부가적으로 추가해주세요!
        if(postSignUpReq.getEmail() == null || postSignUpReq.getEmail() == ""){
            return new BaseResponse<>(USERS_EMPTY_EMAIL);
        }
        if(postSignUpReq.getPassword() == null || postSignUpReq.getPassword() == ""){
            return new BaseResponse<>(USERS_EMPTY_PASSWORD);
        }
        //이메일 정규표현
//        if(!isRegexEmail(postUserReq.getEmail())){
//            return new BaseResponse<>(POST_USERS_INVALID_EMAIL);
//        }
        try{
            PostSignUpRes postSignUpRes = signUpService.createUser(postSignUpReq);
            return new BaseResponse<>(postSignUpRes,SUCCESS_POST_USER);
        } catch(BaseException exception){
            return new BaseResponse<>((exception.getStatus()));
        }
    }

    /**
     * 회원가입 멤버십 등록 API
     * [PATCH] /signUp/membership
     * @return BaseResponse<PatchMembershipRes>
     */
    @ResponseBody
    @PatchMapping("/membership")
    public BaseResponse<PatchMembershipRes> modifyMembership(@RequestBody PatchMembershipReq patchMembershipReq){
        try {
            //jwt에서 idx 추출.
//            int userIdxByJwt = jwtService.getUserId();
//            //userIdx와 접근한 유저가 같은지 확인
//            if(userId != userIdxByJwt){
//                return new BaseResponse<>(INVALID_USER_JWT);
//            }
            //같다면 유저네임 변경
//            PatchUserReq patchUserReq = new PatchUserReq(userIdx,user.getUserName());
            PatchMembershipRes patchMembershipRes = signUpService.modifyMembership(patchMembershipReq);
            return new BaseResponse<>(patchMembershipRes,SUCCESS_MODIFY_NEW_MEMBERSHIP);
        } catch (BaseException exception) {
            exception.printStackTrace();
            return new BaseResponse<>((exception.getStatus()));
        }
    }
    /**
     * 회원가입 결제정보 등록 API
     * [POST] /signUp/credit
     * @return BaseResponse<String>
     */
    @ResponseBody
    @PostMapping("/credit")
    public BaseResponse<PostCreditRes> createCredit(@RequestBody PostCreditReq postCreditReq){
        try {
            //jwt에서 idx 추출.
//            int userIdxByJwt = jwtService.getUserId();
//            //userIdx와 접근한 유저가 같은지 확인
//            if(userId != userIdxByJwt){
//                return new BaseResponse<>(INVALID_USER_JWT);
//            }
            if(postCreditReq.getCreditNo() == null || postCreditReq.getCreditNo() == ""){
                return new BaseResponse<>(EMPTY_CREDIT_NO);
            }
            if(postCreditReq.getExpiryDt() == null || postCreditReq.getExpiryDt() == ""){
                return new BaseResponse<>(EMPTY_EXPIRY_DT);
            }
            if(postCreditReq.getHolderNm() == null || postCreditReq.getHolderNm() == ""){
                return new BaseResponse<>(EMPTY_HOLDER_NM);
            }
            if(postCreditReq.getHolderYear() == null || postCreditReq.getHolderYear() == ""){
                return new BaseResponse<>(EMPTY_HOLDER_YEAR);
            }
            if(postCreditReq.getHolderMonth() == null || postCreditReq.getHolderMonth() == ""){
                return new BaseResponse<>(EMPTY_HOLDER_MONTH);
            }
            if(postCreditReq.getHolderDay() == null || postCreditReq.getHolderDay() == ""){
                return new BaseResponse<>(EMPTY_HOLDER_DAY);
            }

            PostCreditRes postCreditRes = signUpService.createCredit(postCreditReq);
            return new BaseResponse<>(postCreditRes,SUCCESS_POST_CREDIT);
        } catch (BaseException exception) {
            exception.printStackTrace();
            return new BaseResponse<>((exception.getStatus()));
        }
    }


}
