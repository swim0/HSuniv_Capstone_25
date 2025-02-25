package com.book_store.capstone_25.controller;

import com.book_store.capstone_25.model.Rating;
import com.book_store.capstone_25.service.RatingService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/ratings") // <- 평점 API 전체 경로
public class RatingController {

    private final RatingService ratingService;

    public RatingController(RatingService ratingService) {
        this.ratingService = ratingService;
    }

    // POST /ratings <- API 기본 경로로 호출 하시면 됩니다 // 평점 남기는 API
    @PostMapping
    public ResponseEntity<Rating> rateBook(
            @RequestParam Long userId,
            @RequestParam Long bookId,
            @RequestParam int score) { // score는 1~5점 까지 있습니다.
        Rating rating = ratingService.rateBook(userId, bookId, score); // 상세한 코드는 ratingService에 로직이 있습니다.
        return ResponseEntity.ok(rating);
    }

    // GET /ratings/average?bookId=1 // 평점 평균 계산 API
    @GetMapping("/average")
    public ResponseEntity<Double> getAverageRating(@RequestParam Long bookId) {
        double average = ratingService.calculateAverageRating(bookId);  // 상세한 코드는 ratingService에 로직이 있습니다.
        return ResponseEntity.ok(average);
    }
}
