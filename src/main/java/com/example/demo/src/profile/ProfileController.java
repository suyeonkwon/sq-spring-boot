package com.example.demo.src.profile;

import com.example.demo.config.BaseException;
import com.example.demo.config.BaseResponse;
import com.example.demo.src.payment.model.GetPaymentDtlRes;
import com.example.demo.src.payment.model.PatchCreditReq;
import com.example.demo.src.profile.model.GetProfileRes;
import com.example.demo.src.profile.model.PostProfileReq;
import com.example.demo.src.profile.model.PostProfileRes;
import com.example.demo.src.profile.model.Profile;
import com.example.demo.src.signUp.model.PostSignUpReq;
import com.example.demo.src.signUp.model.PostSignUpRes;
import com.example.demo.utils.JwtService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.example.demo.config.BaseResponseStatus.*;

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
    @GetMapping("") // (GET) 127.0.0.1:9000/app/users/:userIdx
    public BaseResponse<List<GetProfileRes>> getProfile(@RequestBody Profile profile) {
        // Get Users
        try{
            List<GetProfileRes> getProfileRes = profileProvider.getProfile(profile.getUserId());
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
}
