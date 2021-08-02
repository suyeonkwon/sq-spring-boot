package com.example.demo.src.setting;


import com.example.demo.config.BaseException;
import com.example.demo.config.secret.Secret;
import com.example.demo.src.profile.model.PatchProfileLockReq;
import com.example.demo.src.setting.model.PatchRatedReq;
import com.example.demo.src.setting.model.PatchViewedHidReq;
import com.example.demo.utils.AES128;
import com.example.demo.utils.JwtService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.example.demo.config.BaseResponseStatus.*;

// Service Create, Update, Delete 의 로직 처리
@Service
@Transactional
public class SettingService {
    final Logger logger = LoggerFactory.getLogger(this.getClass());

    private final SettingDao settingDao;
    private final SettingProvider settingProvider;
    private final JwtService jwtService;


    @Autowired
    public SettingService(SettingDao settingDao, SettingProvider settingProvider, JwtService jwtService) {
        this.settingDao = settingDao;
        this.settingProvider = settingProvider;
        this.jwtService = jwtService;

    }

    public void modifyLock(PatchProfileLockReq patchProfileLockReq) throws BaseException {
        String pin;
        try{
            //암호화
            pin = new AES128(Secret.USER_INFO_PASSWORD_KEY).encrypt(patchProfileLockReq.getPin());
            patchProfileLockReq.setPin(pin);
        } catch (Exception ignored) {
            throw new BaseException(PIN_ENCRYPTION_ERROR);
        }
        try{
            int result = settingDao.modifyLock(patchProfileLockReq);
            if(result == 0){
                throw new BaseException(MODIFY_FAIL_LOCK);
            }
        } catch(Exception exception){
            exception.printStackTrace();
            throw new BaseException(DATABASE_ERROR);
        }
    }

    public void modifyUnLock(PatchProfileLockReq patchProfileLockReq) throws BaseException {
        try{
            int result = settingDao.modifyUnLock(patchProfileLockReq);
            if(result == 0){
                throw new BaseException(MODIFY_FAIL_LOCK);
            }
        } catch(Exception exception){
            exception.printStackTrace();
            throw new BaseException(DATABASE_ERROR);
        }
    }

    public void modifyViewedHid(PatchViewedHidReq patchViewedHidReq) throws BaseException {
        try{
            int result = settingDao.modifyViewedHid(patchViewedHidReq);
            if(result == 0){
                throw new BaseException(MODIFY_FAIL_VIEWED_HIDDEN);
            }
        }catch(Exception exception){
            throw new BaseException(DATABASE_ERROR);
        }
    }

    public void modifyRatedStatus(PatchRatedReq patchRatedReq) throws BaseException {
        try{
            int result = settingDao.modifyRatedStatus(patchRatedReq);
            if(result == 0){
                throw new BaseException(MODIFY_FAIL_RATED_STATUS);
            }
        }catch(Exception exception){
            throw new BaseException(DATABASE_ERROR);
        }

    }

    public void modifyRated(PatchRatedReq patchRatedReq) throws BaseException {
        try{
            int result = settingDao.modifyRated(patchRatedReq);
            if(result == 0){
                throw new BaseException(MODIFY_FAIL_RATED);
            }
        }catch(Exception exception){
            throw new BaseException(DATABASE_ERROR);
        }

    }
}
