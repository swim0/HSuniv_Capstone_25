package com.book_store.capstone_25.Repository;

import com.book_store.capstone_25.model.Cart;
import com.book_store.capstone_25.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;import java.util.Optional;

@Repository
public interface ShoppingCartRepository extends JpaRepository<Cart, Long> {
    Optional<Cart> findByUser(User user);
}