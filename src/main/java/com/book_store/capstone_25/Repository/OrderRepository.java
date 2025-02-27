package com.book_store.capstone_25.Repository;

import com.book_store.capstone_25.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
    Optional<Order> findOrderById(Long orderId);
    List<Order> findOrderByUserId(Long userId);
    List<Order> findByUser_IdOrderByOrderDateDesc(Long userId);

    // 사용자 별 주문 조회, 상태별 주문 조회 등 필요한 커스텀 메서드를 추가할 수 있습니다.
}

