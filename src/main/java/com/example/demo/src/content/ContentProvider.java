package com.example.demo.src.content;

import com.example.demo.config.BaseException;
import com.example.demo.src.content.model.GetContentDtlRes;
import com.example.demo.src.content.model.GetEpisodeRes;
import com.example.demo.src.content.model.GetReleaseRes;
import com.example.demo.src.keep.model.GetKeepRes;
import com.example.demo.utils.JwtService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.example.demo.config.BaseResponseStatus.DATABASE_ERROR;

@Service
public class ContentProvider {
    private final ContentDao contentDao;
    private final JwtService jwtService;


    final Logger logger = LoggerFactory.getLogger(this.getClass());


    public ContentProvider(ContentDao contentDao, JwtService jwtService) {
        this.contentDao = contentDao;
        this.jwtService = jwtService;
    }

    public GetContentDtlRes getContentDtl(int contentId, int profileId) throws BaseException {
        try{
            GetContentDtlRes getContentDtlRes = contentDao.getContentDtl(contentId,profileId);
            return getContentDtlRes;
        }catch (Exception exception){
            exception.printStackTrace();
            throw new BaseException(DATABASE_ERROR);
        }
    }

    public List<GetEpisodeRes> getEpisode(int contentId, int profileId, int season) throws BaseException {
        try{
            List<GetEpisodeRes> getEpisodeRes = contentDao.getEpisode(contentId,profileId,season);
            return getEpisodeRes;
        }catch(Exception exception){
            exception.printStackTrace();
            throw new BaseException(DATABASE_ERROR);
        }

    }

    public List<GetReleaseRes> getRelease(int profileId) throws BaseException {
        try{
            List<GetReleaseRes> getReleaseRes = contentDao.getRelease(profileId);
            return getReleaseRes;
        }catch(Exception exception){
            exception.printStackTrace();
            throw new BaseException(DATABASE_ERROR);
        }
    }
}
