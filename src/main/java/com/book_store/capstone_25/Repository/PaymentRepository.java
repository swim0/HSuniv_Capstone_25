package com.book_store.capstone_25.Repository;

import com.book_store.capstone_25.model.Payment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PaymentRepository extends JpaRepository<Payment, Integer> {
}
