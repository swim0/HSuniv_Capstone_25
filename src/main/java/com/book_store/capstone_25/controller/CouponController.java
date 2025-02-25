package com.book_store.capstone_25.controller;

import com.book_store.capstone_25.DTO.CouponRequest;
import com.book_store.capstone_25.model.Coupon;
import com.book_store.capstone_25.service.CouponService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/coupons")
public class CouponController {
    @Autowired
    private CouponService couponService;

    // 쿠폰 발급 API (관리자가 발급)
    @PostMapping("/create")
    public ResponseEntity<?> createCoupon(@RequestBody CouponRequest request) { // DTO의 CouponRequest를 확인하시면 양식을 보실 수 있습니다.
        Coupon coupon = couponService.createCoupon(request);
        return ResponseEntity.ok(coupon);
    }

    // 특정 사용자 쿠폰 조회 API
    @GetMapping("/user/{userId}")
    public ResponseEntity<?> getUserCoupons(@PathVariable Long userId) {
        List<Coupon> coupons = couponService.getUserCoupons(userId);
        return ResponseEntity.ok(coupons);
    }
}

