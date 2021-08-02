package com.example.demo.src.keep;

import com.example.demo.config.BaseException;
import com.example.demo.config.BaseResponse;
import com.example.demo.src.keep.model.*;
import com.example.demo.utils.JwtService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.example.demo.config.BaseResponseStatus.*;

@RestController
@RequestMapping("/app/keep")
public class KeepController {
    final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private final KeepProvider keepProvider;
    @Autowired
    private final KeepService keepService;
    @Autowired
    private final JwtService jwtService;

    public KeepController(KeepProvider keepProvider, KeepService keepService, JwtService jwtService) {
        this.keepProvider = keepProvider;
        this.keepService = keepService;
        this.jwtService = jwtService;
    }

    /**
     * 찜목록 API
     * [GET] /keep/:profileId
     * @return BaseResponse<List<GetKeepRes>>
     */
    @ResponseBody
    @GetMapping("/{profileId}")
    public BaseResponse<List<GetKeepRes>> getKeep(@PathVariable("profileId") int profileId){
        try{
            List<GetKeepRes> getKeepRes = keepProvider.getKeep(profileId);
            return new BaseResponse<>(getKeepRes,SUCCESS_KEEP_SEARCH);
        } catch(BaseException exception){
            return new BaseResponse<>((exception.getStatus()));
        }
    }

    /**
     * 찜 추가 API
     * [POST] /keep
     * @return BaseResponse<PostKeepRes>
     */
    @ResponseBody
    @PostMapping("")
    public BaseResponse<PostKeepRes> createKeep(@RequestBody PostKeepReq postKeepReq){
        try{
            PostKeepRes postKeepRes = keepService.createKeep(postKeepReq);
            return new BaseResponse<>(postKeepRes,SUCCESS_KEEP_POST);
        } catch(BaseException exception){
            return new BaseResponse<>((exception.getStatus()));
        }
    }

    /**
     * 찜 삭제 API
     * [PATCH] /keep/status
     * @return BaseResponse<>
     */
    @ResponseBody
    @PatchMapping("/status")
    public BaseResponse<String> modifyKeep(@RequestBody PatchKeepReq patchKeepReq) throws BaseException {
        try{
            int profileIdxByJwt = jwtService.getProfileId();
            int profileId = patchKeepReq.getProfileId();
            //userIdx와 접근한 유저가 같은지 확인
            if(profileIdxByJwt != profileId){
                return new BaseResponse<>(INVALID_USER_JWT);
            }
            keepService.modifyKeep(patchKeepReq);
            return new BaseResponse<>(SUCCESS_MODIFY_KEEP_STATUS);
        } catch(BaseException exception){
            return new BaseResponse<>((exception.getStatus()));
        }
    }

}
