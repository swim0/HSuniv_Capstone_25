package com.book_store.capstone_25.controller;

import com.book_store.capstone_25.model.Payment;
import com.book_store.capstone_25.service.PaymentService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/payments") // 결제 처리에 대한 API입니다. 카드,계좌이체 정도가 있으며
                                // User Table에 bank_account가 추가되었습니다. 마찬가지로 마이페이지 에서 변경 가능합니다.
public class PaymentController {

    private final PaymentService paymentService;

    public PaymentController(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    @PostMapping("/pay_process")
    public ResponseEntity<Payment> processPayment(@RequestParam Long orderId,
                                                  @RequestParam String method,
                                                  @RequestParam Long userId,
                                                  @RequestParam(required = false) String couponCode) {
        Payment payment = paymentService.processPayment(orderId, method, userId, couponCode);
        return ResponseEntity.ok(payment);
    }


    @PostMapping("/refund_process")
    public ResponseEntity<Payment> processRefund(@RequestParam Long orderId,
                                                 @RequestParam Long userId) {
        Payment payment = paymentService.refundPayment(orderId, userId);
        return ResponseEntity.ok(payment);
    }
}


