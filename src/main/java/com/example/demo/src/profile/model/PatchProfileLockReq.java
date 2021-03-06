package com.example.demo.src.profile.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class PatchProfileLockReq {
    private int userId;
    private int profileId;
    private String isLock;
    private String pin;
}
