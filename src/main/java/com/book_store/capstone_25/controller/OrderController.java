package com.book_store.capstone_25.controller;

import com.book_store.capstone_25.DTO.OrderRequest;
import com.book_store.capstone_25.model.Order;
import com.book_store.capstone_25.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/orders") // 주문 생성 API 전체 경로
public class OrderController {

    @Autowired
    private OrderService orderService;

    // 주문 생성
    @PostMapping("/create")
    public ResponseEntity<Order> placeOrder(@RequestParam Long userId, @RequestBody OrderRequest request) { // OrderRequest는 DTO에서 양식을 확인하실 수 있습니다.
        Order order = orderService.placeOrder(userId, request); // Service 계층에서 orderService 로직 확인 가능
        return ResponseEntity.ok(order);
    }
}
