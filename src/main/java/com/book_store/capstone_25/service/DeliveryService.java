package com.book_store.capstone_25.service;

import com.book_store.capstone_25.DTO.DeliveryRequest;
import com.book_store.capstone_25.Repository.DeliveryRepository;
import com.book_store.capstone_25.Repository.OrderRepository;
import com.book_store.capstone_25.model.Delivery;
import com.book_store.capstone_25.model.Order;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DeliveryService {
    @Autowired
    private DeliveryRepository deliveryRepository;
    @Autowired
    private OrderRepository orderRepository;

    public Delivery createDelivery(Long orderId, DeliveryRequest request) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("주문을 찾을 수 없습니다."));

        Delivery delivery = new Delivery();
        delivery.setOrder(order);
        delivery.setAddress(request.getAddress());
        delivery.setTrackingNumber(request.getTrackingNumber());
        delivery.setDeliveryStatus("READY");

        return deliveryRepository.save(delivery);
    }
}

