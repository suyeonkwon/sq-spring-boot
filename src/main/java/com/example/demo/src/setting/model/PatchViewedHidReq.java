package com.example.demo.src.setting.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class PatchViewedHidReq {
    private int userId;
    private int recodeId;
}
