package com.book_store.capstone_25.Repository;

import com.book_store.capstone_25.model.User;
import com.book_store.capstone_25.model.User_Interest;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InterestRepository extends JpaRepository<User_Interest, User> {

    void deleteUser_InterestByUserAndGenre(User user, User_Interest.Genre genre);

    // 특정 사용자에 대한 관심사를 찾기
    User_Interest findUserInterestByUserAndGenre(User user, User_Interest.Genre genre);

    // 특정 사용자의 관심사 장르 찾기
    User_Interest findByUserAndGenre(User user, User_Interest.Genre genre);
}