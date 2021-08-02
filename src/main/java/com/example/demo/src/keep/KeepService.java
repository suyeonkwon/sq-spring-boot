package com.example.demo.src.keep;

import com.example.demo.config.BaseException;
import com.example.demo.src.keep.model.PatchKeepReq;
import com.example.demo.src.keep.model.PostKeepReq;
import com.example.demo.src.keep.model.PostKeepRes;
import com.example.demo.utils.JwtService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.example.demo.config.BaseResponseStatus.*;

@Service
@Transactional
public class KeepService {
    final Logger logger = LoggerFactory.getLogger(this.getClass());

    private final KeepDao keepDao;
    private final KeepProvider keepProvider;
    private final JwtService jwtService;


    public KeepService(KeepDao keepDao, KeepProvider keepProvider, JwtService jwtService) {
        this.keepDao = keepDao;
        this.keepProvider = keepProvider;
        this.jwtService = jwtService;
    }

    public PostKeepRes createKeep(PostKeepReq postKeepReq) throws BaseException {
        try {
            int keepId = keepDao.createKeep(postKeepReq);
            return new PostKeepRes(postKeepReq.getProfileId(),keepId);
        } catch (Exception exception) {
            exception.printStackTrace();
            throw new BaseException(DATABASE_ERROR);
        }
    }

    public void modifyKeep(PatchKeepReq patchKeepReq) throws BaseException {
        try{
            int result = keepDao.modifyKeep(patchKeepReq);
            if (result == 0){
                throw new BaseException(MODIFY_FAIL_KEEP_STATUS);
            }
        }catch(Exception exception){
            throw new BaseException(DATABASE_ERROR);
        }
    }
}
