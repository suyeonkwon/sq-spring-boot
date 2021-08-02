package com.example.demo.src.profile;

import com.example.demo.config.BaseException;
import com.example.demo.config.BaseResponse;
import com.example.demo.src.login.model.PostLoginReq;
import com.example.demo.src.login.model.PostLoginRes;
import com.example.demo.src.payment.model.GetPaymentDtlRes;
import com.example.demo.src.payment.model.PatchCreditReq;
import com.example.demo.src.profile.model.*;
import com.example.demo.src.signUp.model.PostSignUpReq;
import com.example.demo.src.signUp.model.PostSignUpRes;
import com.example.demo.utils.JwtService;
import com.fasterxml.jackson.databind.ser.Serializers;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.example.demo.config.BaseResponseStatus.*;
import static com.example.demo.utils.ValidationRegex.isRegexPin;

@RestController
@RequestMapping("/app/profile")
public class ProfileController {
    final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private final ProfileProvider profileProvider;
    @Autowired
    private final ProfileService profileService;
    @Autowired
    private final JwtService jwtService;


    public ProfileController(ProfileProvider profileProvider, ProfileService profileService, JwtService jwtService){
        this.profileProvider = profileProvider;
        this.profileService = profileService;
        this.jwtService = jwtService;
    }
    /**
     * 프로필 조회 API
     * [GET] /profile
     * @return BaseResponse<GetProfileRes>
     */
    // Path-variable
    @ResponseBody
    @GetMapping("")
    public BaseResponse<List<GetProfileRes>> getProfile() {
        // Get Users
        try{
            int userId = jwtService.getUserId();
            List<GetProfileRes> getProfileRes = profileProvider.getProfile(userId);
            return new BaseResponse<>(getProfileRes,SUCCESS_PROFILE_SEARCH);
        } catch(BaseException exception){
            return new BaseResponse<>((exception.getStatus()));
        }
    }
    /**
     * 프로필 등록 API
     * [GET] /profile
     * @return BaseResponse<GetProfileRes>
     */
    @ResponseBody
    @PostMapping("")
    public BaseResponse<PostProfileRes> createProfile(@RequestBody PostProfileReq postProfileReq) {
        if(postProfileReq.getProfileNm() == null || postProfileReq.getProfileNm() == ""){
            return new BaseResponse<>(PROFILE_EMPTY_NAME);
        }
        try{
            PostProfileRes postProfileRes = profileService.createProfile(postProfileReq);
            return new BaseResponse<>(postProfileRes,SUCCESS_POST_PROFILE);
        } catch(BaseException exception){
            return new BaseResponse<>((exception.getStatus()));
        }
    }
    /**
     * 프로필 로그인 API
     * [POST] profile/login
     * @return BaseResponse<PostLoginRes>
     */
    @ResponseBody
    @PostMapping("/login")
    public BaseResponse<PostProfileLoginRes> profileLogIn(@RequestBody PostProfileLoginReq postProfileLoginReq){
        try{
            String isLock = profileProvider.checkLock(postProfileLoginReq.getProfileId());
            postProfileLoginReq.setIsLock(isLock);
            if(isLock.equals("Y")){
                if(postProfileLoginReq.getPin() == null || postProfileLoginReq.getPin() == ""){
                    return new BaseResponse<>(EMPTY_PIN);
                }
                if(!isRegexPin(postProfileLoginReq.getPin())){
                    return new BaseResponse<>(INVALID_PIN);
                }
            }
            PostProfileLoginRes profileLoginRes = profileProvider.logIn(postProfileLoginReq);
            return new BaseResponse<>(profileLoginRes,SUCCESS_PROFILE_LOGIN);
        } catch (BaseException exception){
            return new BaseResponse<>(exception.getStatus());
        }
    }

}
