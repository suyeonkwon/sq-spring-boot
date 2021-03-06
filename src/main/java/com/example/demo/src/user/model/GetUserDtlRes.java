package com.example.demo.src.user.model;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
public class GetUserDtlRes {
    private int userId;
    private String email;
    private String phoneNumber;
    private String membershipCd;
    private String membershipNm;
    private String password;
    private String membershipStartedAt;
    private String isMemberShipUsed;
    private String nextPaymentedAt;
    private String creditNo;
}
