package com.book_store.capstone_25.controller;

import com.book_store.capstone_25.DTO.OrderRequest;
import com.book_store.capstone_25.model.Order;
import com.book_store.capstone_25.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/orders")
public class OrderController {

    @Autowired
    private OrderService orderService;

    // 주문 생성
    @PostMapping("/create")
    public ResponseEntity<Order> placeOrder(@RequestParam Long userId, @RequestBody OrderRequest request) {
        Order order = orderService.placeOrder(userId, request);
        return ResponseEntity.ok(order);
    }
}
