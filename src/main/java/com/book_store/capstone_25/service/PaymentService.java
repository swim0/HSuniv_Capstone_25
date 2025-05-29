package com.book_store.capstone_25.service;

import com.book_store.capstone_25.Repository.OrderRepository;
import com.book_store.capstone_25.Repository.PaymentRepository;
import com.book_store.capstone_25.Repository.UserRepository;
import com.book_store.capstone_25.model.Order;
import com.book_store.capstone_25.model.Payment;
import com.book_store.capstone_25.model.User;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class PaymentService {

    private final PaymentRepository paymentRepository;
    private final OrderRepository orderRepository;
    private final UserRepository userRepository;  // 🔹 UserRepository 추가
    private final CouponService couponService;

    // 🔹 명시적 생성자 선언을 통한 의존성 주입
    public PaymentService(PaymentRepository paymentRepository,
                          OrderRepository orderRepository,
                          UserRepository userRepository, CouponService couponService) {
        this.paymentRepository = paymentRepository;
        this.orderRepository = orderRepository;
        this.userRepository = userRepository;
        this.couponService = couponService;
    }

    @Transactional
    public Payment processPayment(Long orderId, String method, Long userId, String couponCode) {
        // 주문 조회
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("주문을 찾을 수 없습니다."));

        // 중복 결제 방지
        if (order.getStatus().equals("결제 완료")) {
            throw new RuntimeException("이미 결제된 주문입니다.");
        }

        // 사용자 조회
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다."));

        // 결제 수단 검증
        if (method.equalsIgnoreCase("카드")) {
            if (user.getCardNumber() == null || user.getCardType() == null) {
                throw new RuntimeException("카드 정보가 없습니다.");
            }
        } else if (method.equalsIgnoreCase("계좌이체")) {
            if (user.getBankAccount() == null) {
                throw new RuntimeException("계좌 정보가 없습니다.");
            }
        } else {
            throw new RuntimeException("지원하지 않는 결제 방식입니다.");
        }

        // 금액 계산
        double finalAmount = order.getTotalAmount();
        if (couponCode != null && !couponCode.isEmpty()) {
            finalAmount = couponService.applyCoupon(userId, couponCode, finalAmount);
        }

        // 기존 결제 내역 확인
        Optional<Payment> optionalPayment = paymentRepository.findByOrder_Id(orderId);
        Payment payment = optionalPayment.orElse(new Payment());

        payment.setOrder(order);
        payment.setPaymentMethod(method);
        payment.setPaymentDate(LocalDateTime.now());
        payment.setPaid(true);

        // 주문 상태 업데이트
        order.setStatus("결제 완료");
        order.setDiscountedAmount(finalAmount);
        orderRepository.save(order);

        // 쿠폰 적용 처리
        if (couponCode != null && !couponCode.isEmpty()) {
            couponService.applyCouponToOrder(orderId, couponCode);
        }

        return paymentRepository.save(payment);
    }


    /**
     * 환불 기능
     * 환불은 무조건 진행되지만, 만약 주문에 쿠폰이 적용되어 있으면 환불이 불가능합니다.
     */
    @Transactional
    public Payment refundPayment(Long orderId, Long userId) {
        // 주문 조회
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("주문을 찾을 수 없습니다."));

        // 사용자 조회
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다."));

        // 결제 정보 조회
        Payment payment = paymentRepository.findByOrder_Id(orderId)
                .orElseThrow(() -> new RuntimeException("결제 정보를 찾을 수 없습니다."));

        // 무조건 환불 처리
        payment.setPaid(false);
        payment.setPaymentDate(LocalDateTime.now());

        // 주문 상태도 환불 완료로 설정
        order.setStatus("환불 완료");
        orderRepository.save(order);

        return paymentRepository.save(payment);
    }


}
