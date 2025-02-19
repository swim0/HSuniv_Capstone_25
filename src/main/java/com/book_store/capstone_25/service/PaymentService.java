package com.book_store.capstone_25.service;

import com.book_store.capstone_25.Repository.OrderRepository;
import com.book_store.capstone_25.Repository.PaymentRepository;
import com.book_store.capstone_25.Repository.UserRepository;
import com.book_store.capstone_25.model.Order;
import com.book_store.capstone_25.model.Payment;
import com.book_store.capstone_25.model.User;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

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

    public Payment processPayment(Long orderId, String method, String userId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("주문을 찾을 수 없습니다."));

        User user = userRepository.findByUserId(userId)
                .orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다."));

        // 🔹 결제 수단 확인 및 검증
        if (method.equalsIgnoreCase("credit_card")) {
            if (user.getCardNumber() == null || user.getCardType() == null) {
                throw new RuntimeException("카드 정보가 없습니다.");
            }
        } else if (method.equalsIgnoreCase("bank_transfer")) {
            if (user.getBankAccount() == null) {
                throw new RuntimeException("계좌 정보가 없습니다.");
            }
        } else {
            throw new RuntimeException("지원하지 않는 결제 방식입니다.");
        }

        // 🔹 결제 처리
        Payment payment = new Payment();
        payment.setOrder(order);
        payment.setPaymentMethod(method);
        payment.setPaid(true);
        payment.setPaymentDate(LocalDateTime.now());

        order.setStatus("결제완료");

        return paymentRepository.save(payment);
    }
}
