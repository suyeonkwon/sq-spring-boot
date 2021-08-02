package com.example.demo.src.profile.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class Profile {
    private int userId;
    private int profileId;
//    private String profileImgUrl;
    private String pin;
    private String isLock;
}
