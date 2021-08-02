package com.example.demo.src.keep.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class PostKeepReq {
    private int profileId;
    private int contentId;
}
