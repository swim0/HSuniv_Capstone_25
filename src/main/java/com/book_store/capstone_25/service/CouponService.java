package com.book_store.capstone_25.service;

import com.book_store.capstone_25.DTO.CouponRequest;
import com.book_store.capstone_25.Repository.CouponRepository;
import com.book_store.capstone_25.Repository.UserRepository;
import com.book_store.capstone_25.model.Coupon;
import com.book_store.capstone_25.model.User;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
@RequiredArgsConstructor
public class CouponService {

    private final CouponRepository couponRepository;
    private final UserRepository userRepository;



    // 쿠폰 생성 (관리자용)
    public Coupon createCoupon(CouponRequest request) {
        // 사용자 ID 확인 (쿠폰을 발급할 사용자)
        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다."));

        // 쿠폰 생성
        Coupon coupon = new Coupon();
        coupon.setUser(user);
        coupon.setCode(request.getCode());
        coupon.setDiscountAmount(request.getDiscountAmount());
        coupon.setDiscountPercent(request.getDiscountPercent());
        coupon.setMinOrderAmount(request.getMinOrderAmount());
        coupon.setUsed(false); // 처음 발급 시 사용되지 않음
        coupon.setExpiryDate(request.getExpiryDate());

        // 쿠폰 저장
        return couponRepository.save(coupon);
    }
    // 특정 사용자가 가진 쿠폰 조회
    public List<Coupon> getUserCoupons(Long userId) {
        return couponRepository.findByUserId(userId);
    }

    // 쿠폰 검증 및 할인 적용 로직
    public double applyCoupon(Long userId, String couponCode, double orderAmount) {
        // 쿠폰 조회
        Coupon coupon = couponRepository.findByCodeAndUserId(couponCode, userId)
                .orElseThrow(() -> new RuntimeException("유효하지 않은 쿠폰입니다."));

        // 쿠폰이 사용 가능하고 최소 주문 금액을 충족하는지 확인
        if (!coupon.isValid(orderAmount)) {
            throw new RuntimeException("이 쿠폰은 사용할 수 없습니다.");
        }

        // 할인 적용
        double discount = 0;
        if (coupon.getDiscountAmount() > 0) {
            discount = coupon.getDiscountAmount(); // 정액 할인
        } else if (coupon.getDiscountPercent() > 0) {
            discount = orderAmount * (coupon.getDiscountPercent() / 100); // 퍼센트 할인
        }

        // 주문 금액보다 할인 금액이 클 경우 0원으로 조정
        double finalAmount = Math.max(0, orderAmount - discount);

        // 쿠폰을 사용 처리
        coupon.setUsed(true);
        couponRepository.save(coupon);

        return finalAmount; // 최종 결제 금액 반환
    }
}

