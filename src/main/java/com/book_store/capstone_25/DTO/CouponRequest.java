package com.book_store.capstone_25.DTO;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CouponRequest {
    private Long userId;
    private String code;
    private double discountAmount;
    private double discountPercent;
    private double minOrderAmount;
    private LocalDateTime expiryDate;
}
