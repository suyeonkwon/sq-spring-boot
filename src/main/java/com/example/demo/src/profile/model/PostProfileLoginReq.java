package com.example.demo.src.profile.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class PostProfileLoginReq {
    private int userId;
    private int profileId;
    private String pin;
    private String isLock;
}
