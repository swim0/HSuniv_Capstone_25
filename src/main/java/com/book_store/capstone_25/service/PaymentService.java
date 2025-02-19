package com.book_store.capstone_25.service;

import com.book_store.capstone_25.Repository.OrderRepository;
import com.book_store.capstone_25.Repository.PaymentRepository;
import com.book_store.capstone_25.model.Order;
import com.book_store.capstone_25.model.Payment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class PaymentService {
    @Autowired
    private PaymentRepository paymentRepository;
    @Autowired
    private OrderRepository orderRepository;

    public Payment processPayment(Long orderId, String method) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("주문을 찾을 수 없습니다."));

        Payment payment = new Payment();
        payment.setOrder(order);
        payment.setPaymentMethod(method);
        payment.setPaid(true);
        payment.setPaymentDate(LocalDateTime.now());

        order.setStatus("PAID");
        return paymentRepository.save(payment);
    }
}

