package com.book_store.capstone_25.controller;

import com.book_store.capstone_25.model.Payment;
import com.book_store.capstone_25.service.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/payments")
public class PaymentController {
    @Autowired
    private PaymentService paymentService;

    @PostMapping("/pay_process")
    public ResponseEntity<?> processPayment(@RequestParam Long orderId, @RequestParam String method) {
        Payment payment = paymentService.processPayment(orderId, method);
        return ResponseEntity.ok(payment);
    }
}
