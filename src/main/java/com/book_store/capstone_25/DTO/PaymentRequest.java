package com.book_store.capstone_25.DTO;


import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class PaymentRequest {
    private Long orderId;
    private String method;      // "카드" or "계좌이체"
    private Long userId;
    private String couponCode;  // 선택
}