package com.book_store.capstone_25.Repository;

import com.book_store.capstone_25.model.User;
import com.book_store.capstone_25.model.User_Interest;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface InterestRepository extends JpaRepository<User_Interest, User> {
    List<User_Interest> findByUser_IdAndGenresContains(Long userId, User_Interest.Genre genre);
    List<User_Interest> findByUser(User user);

}
