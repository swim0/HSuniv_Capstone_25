package com.book_store.capstone_25.Repository;


import com.book_store.capstone_25.model.Delivery;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DeliveryRepository extends JpaRepository<Delivery, Long> {
}
