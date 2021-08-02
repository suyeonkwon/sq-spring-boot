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
import static com.example.demo.utils.ValidationRegex.isRegexEmail;
import static com.example.demo.utils.ValidationRegex.isRegexPassword;

@RestController
@RequestMapping("/app/signup")
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
     * [POST] /sign-up
     * @return BaseResponse<PostSignUpRes>
     */
    // Body
    @ResponseBody
    @PostMapping("/email")
    public BaseResponse<PostSignUpEmailRes> createUserEmail(@RequestBody PostSignUpReq postSignUpReq) {
        // TODO: email 관련한 짧은 validation 예시입니다. 그 외 더 부가적으로 추가해주세요!
        if(postSignUpReq.getEmail() == null || postSignUpReq.getEmail() == ""){
            return new BaseResponse<>(USERS_EMPTY_EMAIL);
        }
        //이메일 정규표현
        if(!isRegexEmail(postSignUpReq.getEmail())){
            return new BaseResponse<>(POST_USERS_INVALID_EMAIL);
        }

        try{
            PostSignUpEmailRes postSignUpEmailRes = signUpService.createUserEmail(postSignUpReq);
            return new BaseResponse<>(postSignUpEmailRes,SUCCESS_USER_EMAIL);
        } catch(BaseException exception){
            return new BaseResponse<>((exception.getStatus()));
        }
    }

    /**
     * 회원가입 API
     * [POST] /sign-up/password
     * @return BaseResponse<PostSignUpRes>
     */
    // Body
    @ResponseBody
    @PostMapping("/password")
    public BaseResponse<PostSignUpRes> createUser(@RequestBody PostSignUpReq postSignUpReq) {
        if(postSignUpReq.getEmail() == null || postSignUpReq.getEmail() == ""){
            return new BaseResponse<>(USERS_EMPTY_EMAIL);
        }
        if(postSignUpReq.getPassword() == null || postSignUpReq.getPassword() == ""){
            return new BaseResponse<>(USERS_EMPTY_PASSWORD);
        }
        //이메일 정규표현
        if(!isRegexEmail(postSignUpReq.getEmail())){
            return new BaseResponse<>(POST_USERS_INVALID_EMAIL);
        }
        //비밀번호 정규표현 6-60자
        if(!isRegexPassword(postSignUpReq.getPassword())){
            return new BaseResponse<>(POST_USERS_INVALID_PASSWORD);
        }
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
