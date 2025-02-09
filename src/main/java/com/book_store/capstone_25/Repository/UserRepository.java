
package com.book_store.capstone_25.Repository;

import com.book_store.capstone_25.model.User;
import jakarta.persistence.Id;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findUserByUserIdAndPassword(String userId, String password);
    Optional<User> findUserByUserId(String userId);
    Optional<User> findUserByPassword(String password);

    Optional<User> findUserByEmail(String email);

    void deleteUserByUserId(String userId);

    String password(String password);
}
