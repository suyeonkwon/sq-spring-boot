package com.example.demo.src.content.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class GetEpisodeRes {
    private int rowNum;
    private int season;
    private int episodeId;
    private String episodeNm;
    private String runningTime;
    private String summary;
    private String episodeImgUrl;
    private String watchedTime;
}
