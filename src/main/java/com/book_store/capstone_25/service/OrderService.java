package com.book_store.capstone_25.service;

import com.book_store.capstone_25.DTO.OrderRequest;
import com.book_store.capstone_25.Repository.*;
import com.book_store.capstone_25.model.*;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final UserRepository userRepository;
    private final BookRepository bookRepository;
    private final CouponService couponService;
    private final PaymentRepository paymentRepository;
    private final DeliveryRepository deliveryRepository;

    @Transactional
    public Order placeOrder(Long userId, OrderRequest request) {
        // 사용자 조회
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다."));

        double finalPrice = request.getTotalPrice();

        // 쿠폰 적용
        double coupon;
        if (request.getCouponCode() != null && !request.getCouponCode().isEmpty()) {
            coupon = couponService.applyCoupon(userId, request.getCouponCode(), request.getTotalPrice());
            finalPrice = coupon;
        }

        // 주문 생성
        Order order = new Order();
        order.setUser(user);
        order.setTotalAmount(request.getTotalPrice());
        order.setDiscountedAmount(finalPrice);
        order.setOrderDate(LocalDateTime.now());
        order.setStatus("PENDING");

        // 결제 정보 처리
        Payment payment = new Payment();
        payment.setOrder(order);
        payment.setPaid(false); // 예시로 'PENDING' 상태
        payment = paymentRepository.save(payment);
        order.setPayment(payment);

        // 배송 정보 처리
        Delivery delivery = new Delivery();
        delivery.setOrder(order);
        delivery.setDeliveryStatus("PENDING"); // 예시로 'PENDING' 상태
        delivery = deliveryRepository.save(delivery);
        order.setDelivery(delivery);

        // 주문 아이템 처리
        for (OrderRequest.OrderItemRequest itemRequest : request.getItems()) {
            Book book = bookRepository.findById(itemRequest.getBookId())
                    .orElseThrow(() -> new RuntimeException("도서를 찾을 수 없습니다."));

            // 도서명, 수량, 가격 정보 추가
            Order.OrderItemDetails itemDetails = new Order.OrderItemDetails(
                    book.getTitle(), itemRequest.getQuantity(), book.getPrice()
            );

            order.getOrderItems().add(itemDetails);
        }

        // 최종 주문 저장
        return orderRepository.save(order);
    }
}
