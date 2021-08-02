package com.example.demo.src.content.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class GetReleaseRes {
    private int contentId;
    private String contentTitle;
    private String contentExp;
    private String previewUrl;
    private String notiYn;
    private String releasedAt;
}
