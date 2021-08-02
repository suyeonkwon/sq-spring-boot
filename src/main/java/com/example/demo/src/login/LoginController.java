package com.example.demo.src.login;

import com.example.demo.config.BaseException;
import com.example.demo.config.BaseResponse;
import com.example.demo.src.login.model.PostLoginReq;
import com.example.demo.src.login.model.PostLoginRes;
import com.example.demo.utils.JwtService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import static com.example.demo.config.BaseResponseStatus.*;

@RestController
@RequestMapping("/app/login")
public class LoginController {
    final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private final LoginProvider loginProvider;
    @Autowired
    private final LoginService loginService;
    @Autowired
    private final JwtService jwtService;

    public LoginController(LoginProvider loginProvider, LoginService loginService, JwtService jwtService){
        this.loginProvider = loginProvider;
        this.loginService = loginService;
        this.jwtService = jwtService;
    }

    /**
     * 로그인 API
     * [POST] /login
     * @return BaseResponse<PostLoginRes>
     */
    @ResponseBody
    @PostMapping("")
    public BaseResponse<PostLoginRes> logIn(@RequestBody PostLoginReq postLoginReq){
        try{
            // TODO: 로그인 값들에 대한 형식적인 validatin 처리해주셔야합니다!
            // TODO: 유저의 status ex) 비활성화된 유저, 탈퇴한 유저 등을 관리해주고 있다면 해당 부분에 대한 validation 처리도 해주셔야합니다.
            if(postLoginReq.getEmail() == null || postLoginReq.getEmail() == ""){
                return new BaseResponse<>(USERS_EMPTY_EMAIL);
            }
            if(postLoginReq.getPassword() == null || postLoginReq.getPassword() == ""){
                return new BaseResponse<>(USERS_EMPTY_PASSWORD);
            }
            PostLoginRes postLoginRes = loginProvider.logIn(postLoginReq);
            return new BaseResponse<>(postLoginRes,SUCCESS_LOGIN);
        } catch (BaseException exception){
            return new BaseResponse<>(exception.getStatus());
        }
    }

    /**
     * 로그인 API
     * [POST] /login
     * @return BaseResponse<String>
     */
    @ResponseBody
    @GetMapping("/kakao/oauth")
    public BaseResponse<PostLoginRes> logInKakao(@RequestParam("code") String code){
        String access_token = loginService.getAccessToken(code);
        String email = loginProvider.getUserEmail(access_token);

        PostLoginReq postLoginReq = new PostLoginReq();
        postLoginReq.setEmail(email);
        try{
            PostLoginRes postLoginRes = loginProvider.logInKakao(postLoginReq);
            return new BaseResponse<>(postLoginRes,SUCCESS_LOGIN_KAKAO);
        }catch (BaseException exception){
            return new BaseResponse<>(exception.getStatus());
        }
    }

}
