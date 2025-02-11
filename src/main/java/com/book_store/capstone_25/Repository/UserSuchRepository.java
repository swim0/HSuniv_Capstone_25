package com.book_store.capstone_25.Repository;

import com.book_store.capstone_25.model.User_Such;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserSuchRepository extends JpaRepository<User_Such, Long> {
    User_Such findByToken(String token);
}