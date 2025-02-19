package com.book_store.capstone_25.service;

import com.book_store.capstone_25.DTO.OrderRequest;
import com.book_store.capstone_25.Repository.*;
import com.book_store.capstone_25.model.Order;
import com.book_store.capstone_25.model.OrderItem;
import com.book_store.capstone_25.model.User;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;


@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final UserRepository userRepository;
    private final CouponService couponService;

    public Order placeOrder(Long userId, OrderRequest request) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다."));

        double finalPrice = request.getTotalPrice();

        // 쿠폰이 있다면 적용
        if (request.getCouponCode() != null && !request.getCouponCode().isEmpty()) {
            finalPrice = couponService.applyCoupon(userId, request.getCouponCode(), request.getTotalPrice());
        }

        // 주문 생성
        Order order = new Order();
        order.setUser(user);
        order.setTotalAmount(finalPrice);
        order.setOrderDate(LocalDateTime.now());
        order.setStatus("PENDING");

        return orderRepository.save(order);
    }
}


