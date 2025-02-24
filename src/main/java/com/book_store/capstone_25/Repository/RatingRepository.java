package com.book_store.capstone_25.Repository;

import com.book_store.capstone_25.model.Book;
import com.book_store.capstone_25.model.Rating;
import com.book_store.capstone_25.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RatingRepository extends JpaRepository<Rating, Long> {
    // 예를 들어, 특정 도서의 모든 평점을 조회하거나,
    // 특정 사용자가 도서에 남긴 평점을 조회하는 메서드를 추가할 수 있습니다.
    Optional<Rating> findByUserAndBook(User user, Book book);

    List<Rating> findByBook(Book book);
}