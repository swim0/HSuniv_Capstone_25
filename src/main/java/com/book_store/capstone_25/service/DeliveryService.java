package com.book_store.capstone_25.service;

import com.book_store.capstone_25.DTO.DeliveryRequest;
import com.book_store.capstone_25.Repository.DeliveryRepository;
import com.book_store.capstone_25.Repository.OrderRepository;
import com.book_store.capstone_25.model.Delivery;
import com.book_store.capstone_25.model.Order;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class DeliveryService {
    private final DeliveryRepository deliveryRepository;
    private final OrderRepository orderRepository;

    public DeliveryService(DeliveryRepository deliveryRepository, OrderRepository orderRepository) {
        this.deliveryRepository = deliveryRepository;
        this.orderRepository = orderRepository;
    }

    /**
     * 배송 정보 저장 (주문 생성 시 자동 등록)
     */
    public Delivery createDelivery(Long orderId, DeliveryRequest request) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("주문을 찾을 수 없습니다."));

        Delivery delivery = new Delivery();
        delivery.setOrder(order);
        delivery.setAddress(request.getAddress());
        delivery.setTrackingNumber(request.getTrackingNumber());
        delivery.setDeliveryStatus("배송 준비"); // 기본 상태

        return deliveryRepository.save(delivery);
    }

    /**
     * 특정 주문의 배송 정보 조회
     */
    public Delivery getDeliveryByOrderId(Long orderId) {
        return deliveryRepository.findByOrder_Id(orderId)
                .orElseThrow(() -> new RuntimeException("배송 정보를 찾을 수 없습니다."));
    }


     public List<Order> getAllOrdersWithDelivery() {
     List<Order> orders = orderRepository.findAll();

     // 각 주문에 대한 배송 정보 추가
     for (Order order : orders) {
     deliveryRepository.findByOrder_Id(order.getId()).ifPresent(order::setDelivery);
     }

     return orders;
     }


    public Delivery updateDeliveryStatus(Long orderId, String status) {
        Delivery delivery = deliveryRepository.findByOrder_Id(orderId)
                .orElseThrow(() -> new RuntimeException("배송 정보를 찾을 수 없습니다."));

        delivery.setDeliveryStatus(status);
        return deliveryRepository.save(delivery);
    }
}
