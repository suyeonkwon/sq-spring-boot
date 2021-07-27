package com.example.demo.src.signUp.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class PatchMembershipReq {
    private int userId;
    private String membershipCd;
}
