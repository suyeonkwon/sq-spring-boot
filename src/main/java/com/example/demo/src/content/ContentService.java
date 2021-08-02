package com.example.demo.src.content;

import com.example.demo.config.BaseException;
import com.example.demo.src.keep.model.PatchKeepReq;
import com.example.demo.src.keep.model.PostKeepReq;
import com.example.demo.src.keep.model.PostKeepRes;
import com.example.demo.utils.JwtService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.example.demo.config.BaseResponseStatus.DATABASE_ERROR;
import static com.example.demo.config.BaseResponseStatus.MODIFY_FAIL_KEEP_STATUS;

@Service
@Transactional
public class ContentService {
    final Logger logger = LoggerFactory.getLogger(this.getClass());

    private final ContentDao contentDao;
    private final ContentProvider contentProvider;
    private final JwtService jwtService;


    public ContentService(ContentDao contentDao, ContentProvider contentProvider, JwtService jwtService) {
        this.contentDao = contentDao;
        this.contentProvider = contentProvider;
        this.jwtService = jwtService;
    }

}
