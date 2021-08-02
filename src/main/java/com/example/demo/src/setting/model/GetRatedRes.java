package com.example.demo.src.setting.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class GetRatedRes {
    private int profileId;
    private String profileNM;
    private String profileImgUrl;
    private int rateId;
    private String createdAt;
    private int contentId;
    private String contentTitle;
    private String rateGb;
}
