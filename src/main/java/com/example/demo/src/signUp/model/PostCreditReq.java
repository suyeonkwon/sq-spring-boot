package com.example.demo.src.signUp.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class PostCreditReq {
    private int userId;
    private String creditNo;
    private String expiryDt;
    private String holderNm;
    private String holderYear;
    private String holderMonth;
    private String holderDay;
}
