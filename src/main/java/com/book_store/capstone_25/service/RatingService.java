package com.book_store.capstone_25.service;

import com.book_store.capstone_25.Repository.BookRepository;
import com.book_store.capstone_25.Repository.RatingRepository;
import com.book_store.capstone_25.Repository.UserRepository;
import com.book_store.capstone_25.model.Book;
import com.book_store.capstone_25.model.Rating;
import com.book_store.capstone_25.model.User;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class RatingService {

    private final RatingRepository ratingRepository;
    private final BookRepository bookRepository;
    private final UserRepository userRepository;

    public RatingService(RatingRepository ratingRepository, BookRepository bookRepository, UserRepository userRepository) {
        this.ratingRepository = ratingRepository;
        this.bookRepository = bookRepository;
        this.userRepository = userRepository;
    }

    // 사용자가 도서에 평점을 남기는 메서드
    public Rating rateBook(Long userId, Long bookId, int score) {
        if (score < 1 || score > 5) {
            throw new IllegalArgumentException("평점은 1에서 5 사이여야 합니다.");
        }

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new RuntimeException("Book not found"));

        // 이미 평점을 남겼는지 확인하고 업데이트 또는 새로 생성할 수 있습니다.
        Optional<Rating> existingRatingOpt = ratingRepository.findByUserAndBook(user, book);
        Rating rating;
        if (existingRatingOpt.isPresent()) {
            rating = existingRatingOpt.get();
            rating.setScore(score);
        } else {
            rating = new Rating();
            rating.setUser(user);
            rating.setBook(book);
            rating.setScore(score);
        }
        return ratingRepository.save(rating);
    }

    // 도서의 평균 평점을 계산하는 메서드 (추가 기능)
    public double calculateAverageRating(Long bookId) {
        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new RuntimeException("Book not found"));
        List<Rating> ratings = ratingRepository.findByBook(book);
        return ratings.stream()
                .mapToInt(Rating::getScore)
                .average()
                .orElse(0);
    }
}
