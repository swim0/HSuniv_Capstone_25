package com.book_store.capstone_25.service;

import com.book_store.capstone_25.Repository.OrderRepository;
import com.book_store.capstone_25.Repository.PaymentRepository;
import com.book_store.capstone_25.Repository.UserRepository;
import com.book_store.capstone_25.model.Order;
import com.book_store.capstone_25.model.Payment;
import com.book_store.capstone_25.model.User;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class PaymentService {

    private final PaymentRepository paymentRepository;
    private final OrderRepository orderRepository;
    private final UserRepository userRepository;  // 🔹 UserRepository 추가

    // 🔹 명시적 생성자 선언을 통한 의존성 주입
    public PaymentService(PaymentRepository paymentRepository,
                          OrderRepository orderRepository,
                          UserRepository userRepository) {
        this.paymentRepository = paymentRepository;
        this.orderRepository = orderRepository;
        this.userRepository = userRepository;
    }

    public Payment processPayment(Long orderId, String method, Long userId) {
        Order order = orderRepository.findOrderById(orderId)
                .orElseThrow(() -> new RuntimeException("주문을 찾을 수 없습니다."));

        User user = userRepository.findUsersById(userId)
                .orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다."));

        // 🔹 기존 결제 내역 조회
        Optional<Payment> existingPaymentOpt = paymentRepository.findByOrder(order);

        if (existingPaymentOpt.isPresent()) {
            Payment existingPayment = existingPaymentOpt.get();
            if (existingPayment.isPaid()) {
                throw new RuntimeException("이미 결제된 주문입니다. 중복 결제가 불가능합니다.");
            } else {
                // 기존 결제 정보 업데이트
                existingPayment.setPaid(true);
                existingPayment.setPaymentMethod(method);
                existingPayment.setPaymentDate(LocalDateTime.now());
                order.setStatus("결제 완료");
                orderRepository.save(order);
                return paymentRepository.save(existingPayment);
            }
        }

        // 🔹 결제 수단 확인 및 검증
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

        // 🔹 새로운 결제 생성
        Payment payment = new Payment();
        payment.setOrder(order);
        payment.setPaymentMethod(method);
        payment.setPaid(true);
        payment.setPaymentDate(LocalDateTime.now());

        // 주문 상태 업데이트
        order.setStatus("결제 완료");
        orderRepository.save(order);

        return paymentRepository.save(payment);
    }

    /**
     * 환불 기능
     * 환불은 무조건 진행되지만, 만약 주문에 쿠폰이 적용되어 있으면 환불이 불가능합니다.
     */
    public Payment refundPayment(Long orderId, Long userId) {
        // 주문 조회
        Order order = orderRepository.findOrderById(orderId)
                .orElseThrow(() -> new RuntimeException("주문을 찾을 수 없습니다."));

        // 쿠폰 적용 여부 확인
        if (order.getCoupon() != null) {
            throw new RuntimeException("쿠폰이 적용된 주문은 환불이 불가능합니다.");
        }

        // 사용자 조회 (추가 검증용, 필요에 따라 생략 가능)
        User user = userRepository.findUsersById(userId)
                .orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다."));

        // 해당 주문의 결제 정보 조회 (결제와 주문은 1:1 관계로 가정)
        Payment payment = paymentRepository.findByOrder_Id(orderId)
                .orElseThrow(() -> new RuntimeException("결제 정보를 찾을 수 없습니다."));

        // 환불 처리: 예를 들어 주문 상태를 "환불 완료"로 업데이트
        order.setStatus("환불 완료");
        // 주문 상태 업데이트
        orderRepository.save(order);

        return paymentRepository.save(payment);
    }
}
