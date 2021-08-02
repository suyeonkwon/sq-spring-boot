package com.example.demo.src.setting;

import com.example.demo.config.BaseException;
import com.example.demo.config.BaseResponse;
import com.example.demo.src.profile.model.*;
import com.example.demo.src.setting.model.GetRatedRes;
import com.example.demo.src.setting.model.GetViewedRes;
import com.example.demo.src.setting.model.PatchRatedReq;
import com.example.demo.src.setting.model.PatchViewedHidReq;
import com.example.demo.utils.JwtService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.example.demo.config.BaseResponseStatus.*;
import static com.example.demo.utils.ValidationRegex.isRegexPin;

@RestController
@RequestMapping("/app/settings")
public class SettingController {
    final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private final SettingProvider settingProvider;
    @Autowired
    private final SettingService settingService;
    @Autowired
    private final JwtService jwtService;


    public SettingController(SettingProvider settingProvider, SettingService settingService, JwtService jwtService){
        this.settingProvider = settingProvider;
        this.settingService = settingService;
        this.jwtService = jwtService;
    }

    /**
     * 프로필 잠금 API
     * [POST] profile/lock
     * @return BaseResponse<PostLoginRes>
     */
    @ResponseBody
    @PatchMapping("/lock")
    public BaseResponse<PostProfileLoginRes> profileLock(@RequestBody PatchProfileLockReq patchProfileLockReq) throws BaseException {
        //            jwt에서 idx 추출.
        int userIdxByJwt = jwtService.getUserId();
        int userId = patchProfileLockReq.getUserId();
        //userIdx와 접근한 유저가 같은지 확인
        if(userId != userIdxByJwt){
            return new BaseResponse<>(INVALID_USER_JWT);
        }

        try{
            String isLock = patchProfileLockReq.getIsLock();
            if(isLock.equals("Y")){
                if(patchProfileLockReq.getPin() == null || patchProfileLockReq.getPin() == ""){
                    return new BaseResponse<>(EMPTY_PIN);
                }
                if(!isRegexPin(patchProfileLockReq.getPin())){
                    return new BaseResponse<>(INVALID_PIN);
                }
                settingService.modifyLock(patchProfileLockReq);
            }else{
                settingService.modifyUnLock(patchProfileLockReq);
            }
            return new BaseResponse<>(SUCCESS_PROFILE_LOCK);
        } catch (BaseException exception){
            return new BaseResponse<>(exception.getStatus());
        }
    }

    /**
     * 프로필 시청기록 API
     * [POST] settings/viewed/:profileId
     * @return BaseResponse<PostLoginRes>
     */
    @ResponseBody
    @GetMapping("/viewed/{profileId}")
    public BaseResponse<List<GetViewedRes>> getWatched(@PathVariable("profileId") int profileId) throws BaseException {
        try{
            List<GetViewedRes> getViewedRes = settingProvider.getWatched(profileId);
            return new BaseResponse<>(getViewedRes,SUCCESS_VIEWED_SEARCH);
        } catch(BaseException exception){
            return new BaseResponse<>((exception.getStatus()));
        }
    }

    /**
     * 프로필 시청기록 숨김 API
     * [PATCH] /settings/viewed/hidden
     * @return BaseResponse<String>
     */
    @ResponseBody
    @PatchMapping("/viewed/hidden")
    public BaseResponse<String> modifyViewedHid(@RequestBody PatchViewedHidReq patchViewedHidReq){
        try {
            //jwt에서 idx 추출.
            int userIdxByJwt = jwtService.getUserId();
            int userId = patchViewedHidReq.getUserId();
            //userIdx와 접근한 유저가 같은지 확인
            if(userId != userIdxByJwt){
                return new BaseResponse<>(INVALID_USER_JWT);
            }
            settingService.modifyViewedHid(patchViewedHidReq);
            return new BaseResponse<>(SUCCESS_MODIFY_VIEWED_HIDDEN);
        } catch (BaseException exception) {
            exception.printStackTrace();
            return new BaseResponse<>((exception.getStatus()));
        }
    }

    /**
     * 프로필 평가 API
     * [GET] settings/rated/:profileId
     * @return BaseResponse<PostLoginRes>
     */
    @ResponseBody
    @GetMapping("/rated/{profileId}")
    public BaseResponse<List<GetRatedRes>> getRated(@PathVariable("profileId") int profileId) throws BaseException {
        try{
            List<GetRatedRes> getRatedRes = settingProvider.getRated(profileId);
            return new BaseResponse<>(getRatedRes,SUCCESS_RATED_SEARCH);
        } catch(BaseException exception){
            return new BaseResponse<>((exception.getStatus()));
        }
    }

    /**
     * 프로필 평가기록 수정 API
     * [PATCH] /settings/rated
     * @return BaseResponse<String>
     */
    @ResponseBody
    @PatchMapping("/rated")
    public BaseResponse<String> modifyRated(@RequestBody PatchRatedReq patchRatedReq){
        try {
            //jwt에서 idx 추출.
            int userIdxByJwt = jwtService.getUserId();
            int userId = patchRatedReq.getUserId();
            //userIdx와 접근한 유저가 같은지 확인
            if(userId != userIdxByJwt){
                return new BaseResponse<>(INVALID_USER_JWT);
            }

            settingService.modifyRated(patchRatedReq);
            return new BaseResponse<>(SUCCESS_MODIFY_RATED);
        } catch (BaseException exception) {
            exception.printStackTrace();
            return new BaseResponse<>((exception.getStatus()));
        }
    }

    /**
     * 프로필 평가기록 삭제 API
     * [PATCH] /settings/rated/status
     * @return BaseResponse<String>
     */
    @ResponseBody
    @PatchMapping("/rated/status")
    public BaseResponse<String> modifyRatedStatus(@RequestBody PatchRatedReq patchRatedReq){
        try {
            //jwt에서 idx 추출.
            int userIdxByJwt = jwtService.getUserId();
            int userId = patchRatedReq.getUserId();
            //userIdx와 접근한 유저가 같은지 확인
            if(userId != userIdxByJwt){
                return new BaseResponse<>(INVALID_USER_JWT);
            }
            settingService.modifyRatedStatus(patchRatedReq);
            return new BaseResponse<>(SUCCESS_MODIFY_RATED_STATUS);
        } catch (BaseException exception) {
            exception.printStackTrace();
            return new BaseResponse<>((exception.getStatus()));
        }
    }

}
