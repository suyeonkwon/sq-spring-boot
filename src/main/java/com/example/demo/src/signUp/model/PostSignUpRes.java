package com.example.demo.src.signUp.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class PostSignUpRes {
    private int userId;
    private String email;

    public PostSignUpRes(String email) {
        this.email = email;
    }
}
