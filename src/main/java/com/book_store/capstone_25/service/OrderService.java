package com.book_store.capstone_25.service;

import com.book_store.capstone_25.DTO.OrderRequest;
import com.book_store.capstone_25.Repository.*;
import com.book_store.capstone_25.model.*;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final UserRepository userRepository;
    private final BookRepository bookRepository;
    private final CouponService couponService;
    private final PaymentRepository paymentRepository;
    private final DeliveryRepository deliveryRepository;
    private final OrderItemDetailsRepository orderItemDetailsRepository;


    @Transactional
    public Order placeOrder(Long userId, OrderRequest request) {
        // 사용자 조회
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다."));

        double totalPrice = 0.0;
        List<OrderItemDetails> orderItems = new ArrayList<>();

        // 주문 생성 (먼저 저장하여 ID 확보)
        Order order = new Order();
        order.setUser(user);
        order.setOrderDate(LocalDateTime.now());
        order.setStatus("결제 이전");
        order.setAddress(request.getAddress());
        order = orderRepository.save(order); // ✅ 주문 먼저 저장

        // 주문 아이템 처리
        for (OrderRequest.OrderItemRequest itemRequest : request.getItems()) {
            Book book = bookRepository.findBookByBookId(itemRequest.getBookId())
                    .orElseThrow(() -> new RuntimeException("도서를 찾을 수 없습니다."));

            double itemTotalPrice = book.getPrice().doubleValue() * itemRequest.getQuantity();
            totalPrice += itemTotalPrice;

            // ✅ 주문 아이템 생성 (order와 연결!)
            OrderItemDetails itemDetails = new OrderItemDetails(
                    order, book.getBookId(), book.getTitle(), itemRequest.getQuantity(), book.getPrice()
            );
            orderItems.add(itemDetails);
        }

        // ✅ 주문 아이템 저장 (새로운 Repository 사용)
        orderItems.forEach(orderItem -> orderItemDetailsRepository.save(orderItem));

        // 쿠폰 적용
        double discountedPrice = totalPrice;
        if (request.getCouponCode() != null && !request.getCouponCode().isEmpty()) {
            discountedPrice = couponService.applyCoupon(userId, request.getCouponCode(), totalPrice);
        }

        order.setTotalAmount(totalPrice);
        order.setDiscountedAmount(discountedPrice);

        // 결제 정보 생성
        Payment payment = new Payment();
        payment.setOrder(order);
        payment.setPaid(false);
        paymentRepository.save(payment);
        order.setPayment(payment);

        // 배송 정보 생성
        Delivery delivery = new Delivery();
        delivery.setOrder(order);
        delivery.setAddress(order.getAddress());
        delivery.setDeliveryStatus("배송 준비");
        deliveryRepository.save(delivery);
        order.setDelivery(delivery);

        return orderRepository.save(order); // 최종 저장
    }



    public List<Order> getOrdersByUserId(Long userId) {
        return orderRepository.findOrderByUserId(userId);
    }
}
