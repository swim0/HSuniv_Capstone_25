package com.book_store.capstone_25.Repository;

import com.book_store.capstone_25.model.OrderItemDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderItemDetailsRepository extends JpaRepository<OrderItemDetails, Long> {
}
