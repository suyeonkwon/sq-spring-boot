package com.example.demo.src.login;


import com.example.demo.config.BaseException;
import com.example.demo.config.secret.Secret;
import com.example.demo.src.login.model.PostLoginReq;
import com.example.demo.src.login.model.PostLoginRes;
import com.example.demo.src.user.model.*;
import com.example.demo.utils.AES128;
import com.example.demo.utils.JwtService;
import com.fasterxml.jackson.databind.ser.Serializers;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

import static com.example.demo.config.BaseResponseStatus.*;

//Provider : Read의 비즈니스 로직 처리
@Service
public class LoginProvider {

    private final LoginDao loginDao;
    private final JwtService jwtService;


    final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    public LoginProvider(LoginDao loginDao, JwtService jwtService) {
        this.loginDao = loginDao;
        this.jwtService = jwtService;
    }


    public int checkEmail(String email) throws BaseException{
        try{
            return loginDao.checkEmail(email);
        } catch (Exception exception){
            throw new BaseException(DATABASE_ERROR);
        }
    }

    public PostLoginRes logInKakao(PostLoginReq postLoginReq) throws BaseException{
        try{
            //휴면계정
            if(checkUserStatus(postLoginReq.getEmail()).equals("R")){
                throw new BaseException(REST_STATUS_USER);
            }
        }catch(Exception e){
            throw new BaseException(NO_EXIST_USER);
        }

        int userId = loginDao.getPwd(postLoginReq).getUserId();
        String jwt = jwtService.createJwt(userId);
        return new PostLoginRes(userId,jwt);

    }
    public PostLoginRes logIn(PostLoginReq postLoginReq) throws BaseException{
        try{
            //휴면계정
            if(checkUserStatus(postLoginReq.getEmail()).equals("R")){
                throw new BaseException(REST_STATUS_USER);
            }
        }catch(Exception e){
            throw new BaseException(NO_EXIST_USER);
        }


        User user = loginDao.getPwd(postLoginReq);
        String password;
        try {
            password = new AES128(Secret.USER_INFO_PASSWORD_KEY).decrypt(user.getPassword());
        } catch (Exception ignored) {
            throw new BaseException(PASSWORD_DECRYPTION_ERROR);
        }

        if(postLoginReq.getPassword().equals(password)){
            int userId = loginDao.getPwd(postLoginReq).getUserId();
            String jwt = jwtService.createJwt(userId);
            return new PostLoginRes(userId,jwt);
        }
        else{
            throw new BaseException(FAILED_TO_LOGIN);
        }
    }

    //status 확인
    private String checkUserStatus(String email) throws BaseException {
        try{
            return loginDao.checkUserStatus(email);
        } catch (Exception exception){
            throw new BaseException(DATABASE_ERROR);
        }
    }

    public String getUserEmail(String access_Token) {
        String email = "";
        String reqURL = "https://kapi.kakao.com/v2/user/me";
        try {
            URL url = new URL(reqURL);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");

            //    요청에 필요한 Header에 포함될 내용
            conn.setRequestProperty("Authorization", "Bearer " + access_Token);

            int responseCode = conn.getResponseCode();
            System.out.println("responseCode : " + responseCode);

            BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));

            String line = "";
            String result = "";

            while ((line = br.readLine()) != null) {
                result += line;
            }
            System.out.println("response body : " + result);

            JsonParser parser = new JsonParser();
            JsonElement element = parser.parse(result);

            JsonObject properties = element.getAsJsonObject().get("properties").getAsJsonObject();
            JsonObject kakao_account = element.getAsJsonObject().get("kakao_account").getAsJsonObject();

            email = kakao_account.getAsJsonObject().get("email").getAsString();

        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return email;
    }

}
