package com.example.demo.src.user;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.example.demo.config.BaseException;
import com.example.demo.config.BaseResponse;
import com.example.demo.src.user.model.*;
import com.example.demo.utils.JwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;


import static com.example.demo.config.BaseResponseStatus.*;

@RestController
@RequestMapping("/app/users")
public class UserController {
    final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private final UserProvider userProvider;
    @Autowired
    private final UserService userService;
    @Autowired
    private final JwtService jwtService;




    public UserController(UserProvider userProvider, UserService userService, JwtService jwtService){
        this.userProvider = userProvider;
        this.userService = userService;
        this.jwtService = jwtService;
    }

    /**
     * 회원 조회 API
     * [GET] /users
     * 회원 번호 및 이메일 검색 조회 API
     * [GET] /users? Email=
     * @return BaseResponse<List<GetUserRes>>
     */
    //Query String
    @ResponseBody
    @GetMapping("") // (GET) 127.0.0.1:9000/app/users
    public BaseResponse<List<GetUserRes>> getUsers(@RequestParam(required = false) String Email) {
        try{
                List<GetUserRes> getUsersRes = userProvider.getUsers();
                return new BaseResponse<>(getUsersRes,SUCCESS_USER_SEARCH);
        } catch(BaseException exception){
            return new BaseResponse<>((exception.getStatus()));
        }
    }

    /**
     * 회원 1명 조회 API
     * [GET] /users/:userIdx
     * @return BaseResponse<GetUserRes>
     */
    // Path-variable
    @ResponseBody
    @GetMapping("/{userId}") // (GET) 127.0.0.1:9000/app/users/:userIdx
    public BaseResponse<GetUserDtlRes> getUser(@PathVariable("userId") int userId) {
        // Get Users
        try{
            GetUserDtlRes getUserDtlRes = userProvider.getUser(userId);
            return new BaseResponse<>(getUserDtlRes,SUCCESS_USER_SEARCH);
        } catch(BaseException exception){
            return new BaseResponse<>((exception.getStatus()));
        }

    }

    /**
     * 로그인 API
     * [POST] /users/logIn
     * @return BaseResponse<PostLoginRes>
     */
    @ResponseBody
    @PostMapping("/logIn")
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
            PostLoginRes postLoginRes = userProvider.logIn(postLoginReq);
            return new BaseResponse<>(postLoginRes,SUCCESS_LOGIN);
        } catch (BaseException exception){
            return new BaseResponse<>(exception.getStatus());
        }
    }

    /**
     * 유저 멤버십 변경 API
     * [PATCH] /users/membership
     * @return BaseResponse<String>
     */
    @ResponseBody
    @PatchMapping("/membership")
    public BaseResponse<String> modifyMembership(@RequestBody PatchMembershipReq patchMembershipReq){
        try {
            //jwt에서 idx 추출.
//            int userIdxByJwt = jwtService.getUserId();
//            //userIdx와 접근한 유저가 같은지 확인
//            if(userId != userIdxByJwt){
//                return new BaseResponse<>(INVALID_USER_JWT);
//            }
            //같다면 유저네임 변경
//            PatchUserReq patchUserReq = new PatchUserReq(userIdx,user.getUserName());
            userService.modifyMembership(patchMembershipReq);
            String result = "";
            return new BaseResponse<>(result,SUCCESS_MODIFY_MEMBERSHIP);
        } catch (BaseException exception) {
            exception.printStackTrace();
            return new BaseResponse<>((exception.getStatus()));
        }
    }

    /**
     * 유저 이메일주소 변경 API
     * [PATCH] /users/email
     * @return BaseResponse<String>
     */
    @ResponseBody
    @PatchMapping("/email")
    public BaseResponse<String> modifyEmail(@RequestBody PatchEmailReq patchEmailReq){
        try {
            //jwt에서 idx 추출.
//            int userIdxByJwt = jwtService.getUserId();
//            //userIdx와 접근한 유저가 같은지 확인
//            if(userId != userIdxByJwt){
//                return new BaseResponse<>(INVALID_USER_JWT);
//            }
            //같다면 유저네임 변경
//            PatchUserReq patchUserReq = new PatchUserReq(userIdx,user.getUserName());
            if(patchEmailReq.getEmail() == null || patchEmailReq.getEmail() == ""){
                return new BaseResponse<>(USERS_EMPTY_EMAIL);
            }
            userService.modifyEmail(patchEmailReq);
            String result = "";
            return new BaseResponse<>(result,SUCCESS_MODIFY_EMAIL);
        } catch (BaseException exception) {
            exception.printStackTrace();
            return new BaseResponse<>((exception.getStatus()));
        }
    }
    /**
     * 유저 비밀번호 변경 API
     * [PATCH] /users/password
     * @return BaseResponse<String>
     */
    @ResponseBody
    @PatchMapping("/password")
    public BaseResponse<String> modifyPassword(@RequestBody PatchPasswordReq patchPasswordReq){
        try {
            //jwt에서 idx 추출.
//            int userIdxByJwt = jwtService.getUserId();
//            //userIdx와 접근한 유저가 같은지 확인
//            if(userId != userIdxByJwt){
//                return new BaseResponse<>(INVALID_USER_JWT);
//            }
            if(patchPasswordReq.getPassword() == null || patchPasswordReq.getPassword() == ""){
                return new BaseResponse<>(USERS_EMPTY_PASSWORD);
            }
            if(patchPasswordReq.getNewPassword() == null || patchPasswordReq.getNewPassword() == ""){
                return new BaseResponse<>(USERS_EMPTY_NEW_PASSWORD);
            }
            if(patchPasswordReq.getNewPasswordCheck() == null || patchPasswordReq.getNewPasswordCheck() == ""){
                return new BaseResponse<>(USERS_EMPTY_NEW_PASSWORD_CHECK);
            }
            if(!patchPasswordReq.getNewPassword().equals(patchPasswordReq.getNewPasswordCheck())){
                return new BaseResponse<>(USERS_NO_EQUALS_PASSWORD);
            }
            userService.modifyPassword(patchPasswordReq);
            String result = "";
            return new BaseResponse<>(result,SUCCESS_MODIFY_PASSWORD);
        } catch (BaseException exception) {
            exception.printStackTrace();
            return new BaseResponse<>((exception.getStatus()));
        }
    }
    /**
     * 유저 전화번호 변경 API
     * [PATCH] /users/phone
     * @return BaseResponse<String>
     */
    @ResponseBody
    @PatchMapping("/phone")
    public BaseResponse<String> modifyPhone(@RequestBody PatchPhoneReq patchPhoneReq){
        try {
            //jwt에서 idx 추출.
//            int userIdxByJwt = jwtService.getUserId();
//            //userIdx와 접근한 유저가 같은지 확인
//            if(userId != userIdxByJwt){
//                return new BaseResponse<>(INVALID_USER_JWT);
//            }
            //같다면 유저네임 변경
//            PatchUserReq patchUserReq = new PatchUserReq(userIdx,user.getUserName());
            if(patchPhoneReq.getPhoneNumber() == null || patchPhoneReq.getPhoneNumber() == ""){
                return new BaseResponse<>(USERS_EMPTY_PHONE_NUMBER);
            }
            userService.modifyPhone(patchPhoneReq);
            String result = "";
            return new BaseResponse<>(result,SUCCESS_MODIFY_PHONE_NUMBER);
        } catch (BaseException exception) {
            exception.printStackTrace();
            return new BaseResponse<>((exception.getStatus()));
        }
    }


}
