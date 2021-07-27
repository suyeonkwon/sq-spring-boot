package com.example.demo.src.signUp.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class PostSignUpReq {
//    이메일주소
//    비밀번호 6-60자 사이
//    멤버십선택
//    카드 번호
//    만료일 (05/17)
//    이름
//            생년
//    생월
//            생일
    private String email;
    private String password;
}
