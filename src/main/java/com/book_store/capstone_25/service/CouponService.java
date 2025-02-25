package com.book_store.capstone_25.service;

import com.book_store.capstone_25.DTO.CouponRequest;
import com.book_store.capstone_25.Repository.CouponRepository;
import com.book_store.capstone_25.Repository.OrderRepository;
import com.book_store.capstone_25.Repository.UserRepository;
import com.book_store.capstone_25.model.Coupon;
import com.book_store.capstone_25.model.Order;
import com.book_store.capstone_25.model.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CouponService {

    private final CouponRepository couponRepository;
    private final UserRepository userRepository;
    private final OrderRepository orderRepository;

    /**
     * 🔹 쿠폰 생성 (관리자용)
     */
    public Coupon createCoupon(CouponRequest request) {
        User user = userRepository.findUsersById(request.getUserId())
                .orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다."));

        Coupon coupon = new Coupon();
        coupon.setUser(user);
        coupon.setCode(request.getCode());
        coupon.setDiscountAmount(request.getDiscountAmount());
        coupon.setDiscountPercent(request.getDiscountPercent());
        coupon.setMinOrderAmount(request.getMinOrderAmount());
        coupon.setUsed(false); // 초기 상태: 미사용
        coupon.setExpiryDate(request.getExpiryDate());

        return couponRepository.save(coupon);
    }

    /**
     * 🔹 특정 사용자의 쿠폰 목록 조회
     */
    public List<Coupon> getUserCoupons(Long userId) {
        return couponRepository.findByUserId(userId);
    }

    /**
     * 🔹 쿠폰 검증 및 할인 금액 계산 (쿠폰 적용 여부 판단)
     */
    public double applyCoupon(Long userId, String couponCode, double orderAmount) {
        Coupon coupon = couponRepository.findByCodeAndUserId(couponCode, userId)
                .orElseThrow(() -> new RuntimeException("유효하지 않은 쿠폰입니다."));

        // 쿠폰이 사용 가능하고 최소 주문 금액을 충족하는지 확인
        if (!coupon.isValid(orderAmount)) {
            throw new RuntimeException("이 쿠폰은 사용할 수 없습니다.");
        }

        // 할인 적용 계산 (정액 또는 퍼센트)
        double discount = 0;
        if (coupon.getDiscountAmount() > 0) {
            discount = coupon.getDiscountAmount();
        } else if (coupon.getDiscountPercent() > 0) {
            discount = orderAmount * (coupon.getDiscountPercent() / 100);
        }

        return Math.max(0, orderAmount - discount); // 최종 결제 금액 반환
    }

    /**
     * 🔹 결제 완료 시 쿠폰 사용 처리 (결제 시점에 적용)
     */
    public void applyCouponToOrder(Long orderId, String couponCode) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("주문을 찾을 수 없습니다."));

        Coupon coupon = couponRepository.findByCodeAndUserId(couponCode, order.getUser().getId())
                .orElseThrow(() -> new RuntimeException("유효하지 않은 쿠폰입니다."));

        // 쿠폰 사용 처리
        coupon.setUsed(true);
        coupon.setUsedInOrderId(orderId); // 🛑 사용된 주문 ID 저장
        couponRepository.save(coupon);
    }
}
