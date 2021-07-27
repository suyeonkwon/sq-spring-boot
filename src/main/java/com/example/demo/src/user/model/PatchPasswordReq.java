package com.example.demo.src.user.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class PatchPasswordReq {
    private int userId;
    private String password;
    private String newPassword;
    private String newPasswordCheck;
}
