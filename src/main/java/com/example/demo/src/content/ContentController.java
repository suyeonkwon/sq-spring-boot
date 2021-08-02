package com.example.demo.src.content;

import com.example.demo.config.BaseException;
import com.example.demo.config.BaseResponse;
import com.example.demo.src.content.model.GetContentDtlRes;
import com.example.demo.src.content.model.GetEpisodeRes;
import com.example.demo.src.content.model.GetReleaseRes;
import com.example.demo.utils.JwtService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.example.demo.config.BaseResponseStatus.*;

@RestController
@RequestMapping("/app/contents")
public class ContentController {
    final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private final ContentProvider contentProvider;
    @Autowired
    private final ContentService contentService;
    @Autowired
    private final JwtService jwtService;

    public ContentController(ContentProvider contentProvider, ContentService contentService, JwtService jwtService) {
        this.contentProvider = contentProvider;
        this.contentService = contentService;
        this.jwtService = jwtService;
    }

    /**
     * 콘텐츠 상세 API
     * [GET] /contents/:contentId
     * @return BaseResponse<List<GetKeepRes>>
     */
    @ResponseBody
    @GetMapping("/{contentId}")
    public BaseResponse<GetContentDtlRes> getContentDtl(@PathVariable("contentId") int contentId){
        try{
            int profileIdxByJwt = jwtService.getProfileId();
            GetContentDtlRes getContentDtlRes = contentProvider.getContentDtl(contentId,profileIdxByJwt);
            return new BaseResponse<>(getContentDtlRes,SUCCESS_CONTENT_DTL_SEARCH);
        } catch(BaseException exception){
            return new BaseResponse<>((exception.getStatus()));
        }
    }
    /**
     * 콘텐츠 에피소드 API
     * [GET] /contents/:contentId/episode
     * @return BaseResponse<List<GetKeepRes>>
     */
    @ResponseBody
    @GetMapping("/{contentId}/episode/{season}")
    public BaseResponse<List<GetEpisodeRes>> getEpisode(@PathVariable("contentId") int contentId, @PathVariable("season") int season){
        try{
            int profileIdxByJwt = jwtService.getProfileId();
            List<GetEpisodeRes> getEpisodeRes = contentProvider.getEpisode(contentId,profileIdxByJwt,season);
            return new BaseResponse<>(getEpisodeRes,SUCCESS_CONTENT_EPISODE);
        } catch(BaseException exception){
            return new BaseResponse<>((exception.getStatus()));
        }
    }

    /**
     * 콘텐츠 공개예정 API
     * [GET] /contents/release
     * @return BaseResponse<List<GetKeepRes>>
     */
    @ResponseBody
    @GetMapping("/release")
    public BaseResponse<List<GetReleaseRes>> getRelease(){
        try{
            int profileIdxByJwt = jwtService.getProfileId();
            List<GetReleaseRes> getReleaseRes = contentProvider.getRelease(profileIdxByJwt);
            return new BaseResponse<>(getReleaseRes,SUCCESS_CONTENT_RELEASE);
        } catch(BaseException exception){
            return new BaseResponse<>((exception.getStatus()));
        }
    }
}
