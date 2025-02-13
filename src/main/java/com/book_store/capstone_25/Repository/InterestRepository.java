package com.book_store.capstone_25.Repository;

import com.book_store.capstone_25.model.User;
import com.book_store.capstone_25.model.User_Interest;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface InterestRepository extends JpaRepository<User_Interest, User> {
    Optional<User_Interest> findByUser_IdAndGenresContains(Long userId, User_Interest.Genre genre);
    Optional<User_Interest> findByUser_Id(Long userId);
    Optional<User_Interest> findByUser(User user);

}
