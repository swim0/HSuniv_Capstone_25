package com.book_store.capstone_25.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "orders")
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user; // 주문한 사용자

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL)
    private List<OrderItem> orderItems = new ArrayList<>(); // 주문 상세 목록

    @OneToOne(mappedBy = "order", cascade = CascadeType.ALL)
    private Payment payment; // 결제 정보

    @OneToOne(mappedBy = "order", cascade = CascadeType.ALL)
    private Delivery delivery; // 배송 정보

    private LocalDateTime orderDate; // 주문 날짜
    private String status; // 주문 상태 (예: PENDING, PAID, SHIPPED, DELIVERED)


    @OneToOne
    @JoinColumn(name = "coupon_id")
    private Coupon coupon; // 적용된 쿠폰

    private double totalAmount; // 주문 총 금액
    private double discountedAmount; // 할인 후 최종 금액

}
