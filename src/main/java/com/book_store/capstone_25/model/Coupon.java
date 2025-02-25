package com.book_store.capstone_25.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "coupons")
public class Coupon {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user; // 쿠폰 소유자

    private String code; // 쿠폰 코드 (예: "DISCOUNT10")
    private double discountAmount; // 정액 할인 금액 (예: 5000원)
    private double discountPercent; // 비율 할인 (예: 10%)
    private double minOrderAmount; // 최소 주문 금액 제한
    private boolean isUsed; // 사용 여부

    private LocalDateTime expiryDate; // 만료 날짜
    @Column(name = "used_in_order_id")
    private Long usedInOrderId;
    // 쿠폰이 사용 가능한 상태인지 확인
    public boolean isValid(double orderAmount) {
        return !isUsed && expiryDate.isAfter(LocalDateTime.now()) && orderAmount >= minOrderAmount;
    }
}
