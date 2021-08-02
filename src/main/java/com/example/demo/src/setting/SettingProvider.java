package com.example.demo.src.setting;


import com.example.demo.config.BaseException;
import com.example.demo.config.secret.Secret;
import com.example.demo.src.profile.model.GetProfileRes;
import com.example.demo.src.profile.model.PostProfileLoginReq;
import com.example.demo.src.profile.model.PostProfileLoginRes;
import com.example.demo.src.profile.model.Profile;
import com.example.demo.src.setting.model.GetRatedRes;
import com.example.demo.src.setting.model.GetViewedRes;
import com.example.demo.utils.AES128;
import com.example.demo.utils.JwtService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.example.demo.config.BaseResponseStatus.*;

//Provider : Read의 비즈니스 로직 처리
@Service
public class SettingProvider {

    private final SettingDao settingDao;
    private final JwtService jwtService;


    final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    public SettingProvider(SettingDao settingDao, JwtService jwtService) {
        this.settingDao = settingDao;
        this.jwtService = jwtService;
    }

    public String checkLock(int profileId) throws BaseException {
        try {
            String checkLock = settingDao.checkLock(profileId);
            return checkLock;
        } catch (Exception exception) {
            exception.printStackTrace();
            throw new BaseException(DATABASE_ERROR);
        }

    }

    public List<GetViewedRes> getWatched(int profileId) throws BaseException {
        try {
            List<GetViewedRes> getViewedRes = settingDao.getWatched(profileId);
            return getViewedRes;
        } catch (Exception exception) {
            exception.printStackTrace();
            throw new BaseException(DATABASE_ERROR);
        }
    }

    public List<GetRatedRes> getRated(int profileId) throws BaseException {
        try {
            List<GetRatedRes> getRatedRes = settingDao.getRated(profileId);
            return getRatedRes;
        } catch (Exception exception) {
            exception.printStackTrace();
            throw new BaseException(DATABASE_ERROR);
        }
    }
}
