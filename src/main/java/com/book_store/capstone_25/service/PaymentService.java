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

        // 중복 결제 방지 (이미 결제된 주문인지 확인)
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

        // 최종 결제 금액 계산 (쿠폰 적용 등)
        double finalAmount = order.getTotalAmount(); // 기본 금액
        if (couponCode != null && !couponCode.isEmpty()) {
            finalAmount = couponService.applyCoupon(userId, couponCode, finalAmount);
        }

        // Payment 레코드가 이미 존재하는지 확인
        Optional<Payment> existingPaymentOpt = paymentRepository.findByOrder(order);
        Payment payment;
        if (existingPaymentOpt.isPresent()) {
            // 이미 생성된 Payment가 있다면 업데이트
            payment = existingPaymentOpt.get();
            // 결제 상태 업데이트
            payment.setPaid(true);
            payment.setPaymentMethod(method);
            payment.setPaymentDate(LocalDateTime.now());
        } else {
            // 없으면 새 Payment 생성
            payment = new Payment();
            payment.setOrder(order);
            payment.setPaymentMethod(method);
            payment.setPaid(true);
            payment.setPaymentDate(LocalDateTime.now());
        }

        // 주문 상태 업데이트
        order.setStatus("결제 완료");
        order.setDiscountedAmount(finalAmount);
        orderRepository.save(order);

        // 쿠폰 적용 (결제 완료 후 쿠폰 사용 처리)
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

        // 🛑 이미 환불된 주문인지 확인
        if (order.getStatus().equals("환불 완료")) {
            throw new RuntimeException("이미 환불된 주문입니다.");
        }

        // 🛑 쿠폰이 적용된 경우 환불 불가
        if (order.getCoupon() != null) {
            throw new RuntimeException("쿠폰이 적용된 주문은 환불이 불가능합니다.");
        }

        // 사용자 조회 (추가 검증용)
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다."));

        // 해당 주문의 결제 정보 조회 (결제와 주문은 1:1 관계)
        Payment payment = paymentRepository.findByOrder_Id(orderId)
                .orElseThrow(() -> new RuntimeException("결제 정보를 찾을 수 없습니다."));

        // 🛑 결제되지 않은 주문을 환불 요청한 경우 예외 발생
        if (!payment.isPaid()) {
            throw new RuntimeException("결제되지 않은 주문은 환불할 수 없습니다.");
        }

        // 🛑 환불 처리: 결제 상태를 취소로 변경
        payment.setPaid(false); // 결제 취소
        payment.setPaymentDate(LocalDateTime.now()); // 환불된 시점으로 갱신

        // 🛑 주문 상태 업데이트 (환불 완료)
        order.setStatus("환불 완료");
        orderRepository.save(order);

        return paymentRepository.save(payment);
    }

}
