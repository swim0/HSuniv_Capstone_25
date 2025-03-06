package com.book_store.capstone_25.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "search_history")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SearchHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)  // 검색한 사용자
    private User user;

    private String title;
    private String author;
    private String publisher;
    private String genre;

    @Column(nullable = false)
    private String keyword; // 🔹 검색어 필드 추가

    private LocalDateTime searchedAt;

    @Builder
    public SearchHistory(User user, String keyword, String title, String author, String publisher, String genre) {
        this.user = user;
        this.keyword = keyword;
        this.title = title;
        this.author = author;
        this.publisher = publisher;
        this.genre = genre;
    }


    // ✅ 생성 시 자동으로 searchedAt 설정
    @PrePersist
    public void prePersist() {
        this.searchedAt = LocalDateTime.now();
    }
}
