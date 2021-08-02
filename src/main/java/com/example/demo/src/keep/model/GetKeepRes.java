package com.example.demo.src.keep.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class GetKeepRes {
    private int profileId;
    private int keepId;
    private int contentId;
    private String contentTitle;
    private String contentImgUrl;
}
