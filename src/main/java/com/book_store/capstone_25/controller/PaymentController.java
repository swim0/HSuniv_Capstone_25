package com.book_store.capstone_25.controller;

import com.book_store.capstone_25.DTO.PaymentRequest;
import com.book_store.capstone_25.model.Payment;
import com.book_store.capstone_25.service.PaymentService;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/payments")
public class PaymentController {

    private final PaymentService paymentService;
    public PaymentController(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    /** ------------------------------------------------------------------
     *  ①  application/x-www-form-urlencoded  또는  query‑string  전용
     * ------------------------------------------------------------------ */
    @PostMapping(
            value = "/pay_process",
            consumes = { MediaType.APPLICATION_FORM_URLENCODED_VALUE, MediaType.ALL_VALUE }
    )
    public ResponseEntity<?> payProcessForm(@ModelAttribute PaymentRequest req) {
        return wrap(() -> paymentService.processPayment(
                req.getOrderId(), req.getMethod(), req.getUserId(), req.getCouponCode()));
    }

    /** ------------------------------------------------------------------
     *  ②  application/json  전용  (프론트가 나중에 JSON 전송으로 바꿔도 OK)
     * ------------------------------------------------------------------ */
    @PostMapping(
            value = "/pay_process",
            consumes = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<?> payProcessJson(@RequestBody PaymentRequest req) {
        return wrap(() -> paymentService.processPayment(
                req.getOrderId(), req.getMethod(), req.getUserId(), req.getCouponCode()));
    }

    // ---------------- 환불 ----------------
    @PostMapping("/refund_process")
    public ResponseEntity<?> refund(@RequestParam Long orderId,
                                    @RequestParam Long userId) {
        return wrap(() -> paymentService.refundPayment(orderId, userId));
    }

    /*===== 공통 응답 래퍼 =====*/
    private ResponseEntity<?> wrap(java.util.concurrent.Callable<Payment> call) {
        try {
            Payment p = call.call();
            return ResponseEntity.ok(p);
        } catch (Exception e) {
            System.err.println("❌ 결제/환불 실패: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
