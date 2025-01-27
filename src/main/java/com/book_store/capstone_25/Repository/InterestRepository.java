package com.book_store.capstone_25.Repository;

import com.book_store.capstone_25.model.User;
import com.book_store.capstone_25.model.UserInterest;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InterestRepository extends JpaRepository<UserInterest, Long> {

    // 특정 사용자의 특정 관심사를 삭제
    void deleteUserInterestByUserAndId(User user, Long id);

    // 특정 사용자에 대한 관심사를 찾기
    UserInterest findUserInterestByUser(User user);
}
