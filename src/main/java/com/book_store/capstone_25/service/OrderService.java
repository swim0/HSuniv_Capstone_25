package com.book_store.capstone_25.service;

import com.book_store.capstone_25.DTO.OrderRequest;
import com.book_store.capstone_25.Repository.*;
import com.book_store.capstone_25.model.*;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;

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

        double totalPrice = 0.0; // 🛑 총 가격 자동 계산
        ArrayList<Order.OrderItemDetails> orderItems = new ArrayList<>();

        // 주문 아이템 처리
        for (OrderRequest.OrderItemRequest itemRequest : request.getItems()) {
            Book book = bookRepository.findBookByBookId(itemRequest.getBookId())
                    .orElseThrow(() -> new RuntimeException("도서를 찾을 수 없습니다."));

            // 개별 도서 가격 계산 (가격 * 수량)
            double itemTotalPrice = book.getPrice().doubleValue() * itemRequest.getQuantity();
            totalPrice += itemTotalPrice;

            // 주문 아이템 생성
            Order.OrderItemDetails itemDetails = new Order.OrderItemDetails(
                    book.getBookId(), book.getTitle(), itemRequest.getQuantity(), book.getPrice()
            );
            orderItems.add(itemDetails);
        }

        // 쿠폰 적용
        double discountedPrice = totalPrice;
        if (request.getCouponCode() != null && !request.getCouponCode().isEmpty()) {
            discountedPrice = couponService.applyCoupon(userId, request.getCouponCode(), totalPrice);
        }

        // 주문 생성
        Order order = new Order();
        order.setUser(user);
        order.setOrderItems(orderItems);
        order.setTotalAmount(totalPrice); // 🛑 총 가격 자동 계산
        order.setDiscountedAmount(discountedPrice); // 🛑 할인 적용된 금액
        order.setOrderDate(LocalDateTime.now());
        order.setStatus("결제 이전");
        order.setAddress(request.getAddress());

        order = orderRepository.save(order); // 🛑 먼저 주문 저장 (ID 생성)

        // 결제 정보 처리
        Payment payment = new Payment();
        payment.setOrder(order);
        payment.setPaid(false);
        paymentRepository.save(payment);
        order.setPayment(payment);

        // 🛑 배송 정보 자동 등록
        Delivery delivery = new Delivery();
        delivery.setOrder(order);
        delivery.setAddress(order.getAddress());
        delivery.setDeliveryStatus("배송 준비");
        deliveryRepository.save(delivery);
        order.setDelivery(delivery);

        return orderRepository.save(order);
    }
}
