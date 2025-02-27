package com.book_store.capstone_25.controller;

import com.book_store.capstone_25.model.Delivery;
import com.book_store.capstone_25.model.Order;
import com.book_store.capstone_25.service.DeliveryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/delivery") // 배송 조회 API 전체 경로
// 배송 정보 조회입니다. 주석 처리한 코드들은 필요하신 경우 사용해주세요
@RequiredArgsConstructor
public class DeliveryController {
    private final DeliveryService deliveryService;

    /**
     * 특정 주문의 배송 정보 조회
     */
    @GetMapping("/order/{orderId}")
    public ResponseEntity<Delivery> getDeliveryByOrderId(@PathVariable Long orderId) {
        return ResponseEntity.ok(deliveryService.getDeliveryByOrderId(orderId));
    }



    /**
     * 배송 정보 추가 (배송 준비 상태) -> 결제시 자동 배송 준비로 바뀜
     */
    /**
    @PostMapping("/order/{orderId}")
    public ResponseEntity<Delivery> createDelivery(
            @PathVariable Long orderId, @RequestBody DeliveryRequest request) {
        return ResponseEntity.ok(deliveryService.createDelivery(orderId, request));
    }
    */


    /**
     * 배송 상태 업데이트 (예: 배송중, 배송완료) -> 실제 서비스가 아니므로 "배송준비" 상태만 띄워져 있음
     */
    /**
    @PutMapping("/{orderId}/status")
    public ResponseEntity<Delivery> updateDeliveryStatus(
            @PathVariable Long orderId, @RequestParam String status) {
        return ResponseEntity.ok(deliveryService.updateDeliveryStatus(orderId, status));
            }
     */
}
