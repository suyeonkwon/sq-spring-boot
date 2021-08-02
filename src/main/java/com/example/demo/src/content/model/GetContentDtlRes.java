package com.example.demo.src.content.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class GetContentDtlRes {
    private int contentId;
    private String contentTitle;
    private String keepYn;
    private String rateGb;
    private String previewUrl;
    private String contentExp;
    private String age;
    private String releasedAt;
    private int season;
    private String actor;
    private String genre;
    private String charct;
}
