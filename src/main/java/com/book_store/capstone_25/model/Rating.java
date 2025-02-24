package com.book_store.capstone_25.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "ratings")
public class Rating {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 도서와 다대일 관계: 하나의 도서에 여러 평점
    @ManyToOne
    @JoinColumn(name = "book_id")
    private Book book;

    // 사용자와 다대일 관계: 하나의 사용자가 여러 도서에 평점을 남길 수 있음
    // (또한, 한 도서에 한 번만 평점을 남기게 할 수도 있음 - 비즈니스 로직에 따라 처리)
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    // 평점 값 (1~5 사이)
    private int score;

    // 생성자, getter, setter 등 필요
}
