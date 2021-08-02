package com.example.demo.src.setting.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class PatchRatedReq {
    private int userId;
    private int profileId;
    private int rateId;
    private int contentId;
    private String rateGb;
}
