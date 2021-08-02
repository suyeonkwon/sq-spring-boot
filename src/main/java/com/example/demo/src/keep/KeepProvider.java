package com.example.demo.src.keep;

import com.example.demo.config.BaseException;
import com.example.demo.src.keep.model.GetKeepRes;
import com.example.demo.src.keep.model.PostKeepReq;
import com.example.demo.src.keep.model.PostKeepRes;
import com.example.demo.utils.JwtService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.example.demo.config.BaseResponseStatus.DATABASE_ERROR;

@Service
public class KeepProvider {
    private final KeepDao keepDao;
    private final JwtService jwtService;


    final Logger logger = LoggerFactory.getLogger(this.getClass());


    public KeepProvider(KeepDao keepDao, JwtService jwtService) {
        this.keepDao = keepDao;
        this.jwtService = jwtService;
    }

    public List<GetKeepRes> getKeep(int profileId) throws BaseException {
        try {
            List<GetKeepRes> getKeepRes = keepDao.getKeep(profileId);
            return getKeepRes;
        } catch (Exception exception) {
            exception.printStackTrace();
            throw new BaseException(DATABASE_ERROR);
        }

    }
}
