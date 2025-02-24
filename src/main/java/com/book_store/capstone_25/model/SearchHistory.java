package com.book_store.capstone_25.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "search_history")
@Getter
@Setter
@NoArgsConstructor
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

    private LocalDateTime searchedAt;

    public SearchHistory(User user, String title, String author, String publisher, String genre) {
        this.user = user;
        this.title = title;
        this.author = author;
        this.publisher = publisher;
        this.genre = genre;
        this.searchedAt = LocalDateTime.now();
    }
}
