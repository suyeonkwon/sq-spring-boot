package com.example.demo.src.payment.model;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
public class GetPaymentDtlRes {
    private int userId;
    private String paymentedAt;
    private String nextMonth;
    private String remark;
    private String creditNo;
    private String price;
    private String vat;
    private String totalPrice;
}
