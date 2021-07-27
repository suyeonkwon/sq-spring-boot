package com.example.demo.src.signUp.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
public class PatchMembershipRes {
    private int userId;
    private String membershipCd;
    private Date membershipStartedAt;
}
