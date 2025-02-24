package com.book_store.capstone_25.Repository;

import com.book_store.capstone_25.model.Order;
import com.book_store.capstone_25.model.Payment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PaymentRepository extends JpaRepository<Payment, Integer> {
    Optional<Payment> findByOrder_Id(Long orderId);
    Optional<Payment> findByOrder(Order order);
}
