package com.example.demo.src.payment.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class GetPaymentMembershipRes {
    private int userId;
    private String membership;
    private String simultanConn;
    private String nextMonth;
}
