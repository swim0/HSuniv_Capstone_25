package com.book_store.capstone_25.controller;

import com.book_store.capstone_25.DTO.OrderRequest;
import com.book_store.capstone_25.model.Order;
import com.book_store.capstone_25.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/orders") // 주문 생성 API 전체 경로
public class OrderController {

    @Autowired
    private OrderService orderService;

    @PostMapping("/create")
    public ResponseEntity<?> placeOrder(@RequestParam Long userId, @RequestBody OrderRequest request) {
        System.out.println("Received OrderRequest: " + request);
        System.out.println("Order Items: " + request.getItems());

        if (request.getItems() == null || request.getItems().isEmpty()) {
            return ResponseEntity.badRequest().body("주문 아이템이 비어 있습니다.");
        }

        Order order = orderService.placeOrder(userId, request);
        return ResponseEntity.ok(order);
    }
    @PostMapping(value="/create",
            consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public ResponseEntity<?> placeOrderForm(@ModelAttribute OrderRequest request,
                                            @RequestParam Long userId) {
        Order order = orderService.placeOrder(userId, request);
        return ResponseEntity.ok(order);
    }

    // 주문 전체 확인
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Order>> getUserOrders(@PathVariable Long userId) {
        List<Order> orders = orderService.getOrdersByUserId(userId);
        return ResponseEntity.ok(orders);
    }

}
