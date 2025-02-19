package com.book_store.capstone_25.controller;

import com.book_store.capstone_25.DTO.OrderRequest;
import com.book_store.capstone_25.model.Order;
import com.book_store.capstone_25.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    @PostMapping("/create") // 주문 생성입니다. 도서 구매 기능에 속합니다
    public ResponseEntity<?> createOrder(@RequestParam Long userId, @RequestBody OrderRequest request) {
        Order order = orderService.placeOrder(userId, request);
        return ResponseEntity.ok(order);
    }
}

