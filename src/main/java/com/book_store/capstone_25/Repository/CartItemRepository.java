package com.book_store.capstone_25.Repository;

import com.book_store.capstone_25.model.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CartItemRepository extends JpaRepository<CartItem, Long> {
    // 필요에 따라 추가적인 쿼리 메서드를 정의할 수 있습니다.
}