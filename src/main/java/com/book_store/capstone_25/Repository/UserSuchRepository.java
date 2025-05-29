package com.book_store.capstone_25.Repository;

import com.book_store.capstone_25.model.User;
import com.book_store.capstone_25.model.User_Such;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserSuchRepository extends JpaRepository<User_Such, Long> {
    User_Such findUser_SuchByToken(String token);

    User_Such findUser_SuchByUserEmailAndAuthenticationCode(String userEmail, String authenticationCode);
    Optional<User_Such> findByUser(User user);
    void deleteByUser(User user);
}