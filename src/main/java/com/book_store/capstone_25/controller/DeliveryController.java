package com.book_store.capstone_25.controller;

import com.book_store.capstone_25.DTO.DeliveryRequest;
import com.book_store.capstone_25.model.Delivery;
import com.book_store.capstone_25.service.DeliveryService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/deliveries")
@RequiredArgsConstructor
public class DeliveryController {

    private final DeliveryService deliveryService;

    @PostMapping("/create") // 배송 현황에 속하는 컨트롤러입니다.
    public ResponseEntity<?> createDelivery(@RequestParam Long orderId, @RequestBody DeliveryRequest request) {
        Delivery delivery = deliveryService.createDelivery(orderId, request);
        return ResponseEntity.ok(delivery);
    }
}
