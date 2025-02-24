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
        if (request.getCouponCode() != null && !request.getCouponCode().isEmpty()) {
            finalPrice = couponService.applyCoupon(userId, request.getCouponCode(), request.getTotalPrice());
        }

        // 주문 생성 (먼저 저장해야 함)
        Order order = new Order();
        order.setUser(user);
        order.setTotalAmount(request.getTotalPrice());
        order.setDiscountedAmount(finalPrice);
        order.setOrderDate(LocalDateTime.now());
        order.setStatus("결제 이전");

        order = orderRepository.save(order);  //  먼저 영속화하여 ID를 생성해야 함!

        //  이후에 연관 엔티티를 저장해야 함! (Order 저장 후 Payment, Delivery 생성)

        // 결제 정보 처리 (Order 저장 후 진행)
        Payment payment = new Payment();
        payment.setOrder(order);
        payment.setPaid(false); // 결제 대기 상태
        paymentRepository.save(payment);  // 🚀 여기서 Payment 저장 (Order가 DB에 저장된 후!)

        order.setPayment(payment);

        // 배송 정보 처리
        Delivery delivery = new Delivery();
        delivery.setOrder(order);
        delivery.setDeliveryStatus("배송 이전");
        deliveryRepository.save(delivery);  // 🚀 여기서 Delivery 저장 (Order가 DB에 저장된 후!)

        order.setDelivery(delivery);

        // 주문 아이템 처리
        for (OrderRequest.OrderItemRequest itemRequest : request.getItems()) {
            Book book = bookRepository.findBookByBookId(itemRequest.getBookId())
                    .orElseThrow(() -> new RuntimeException("도서를 찾을 수 없습니다."));

            // 도서명, 수량, 가격 정보 추가
            Order.OrderItemDetails itemDetails = new Order.OrderItemDetails(
                    book.getBookId(), book.getTitle(), itemRequest.getQuantity(), book.getPrice()
            );

            order.getOrderItems().add(itemDetails);
        }

        // 🔹 Order 최종 업데이트
        return orderRepository.save(order);
    }
}
