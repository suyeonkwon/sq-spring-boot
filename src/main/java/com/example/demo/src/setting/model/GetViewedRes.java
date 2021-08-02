package com.example.demo.src.setting.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class GetViewedRes {
    private int profileId;
    private String profileNM;
    private String profileImgUrl;
    private int recodeId;
    private int episodeId;
    private String watchedAt;
    private String title;
}
