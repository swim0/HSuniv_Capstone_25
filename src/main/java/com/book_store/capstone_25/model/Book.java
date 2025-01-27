package com.book_store.capstone_25.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "books")
public class Book {

    @ManyToOne
    @JoinColumn(name = "book_id", nullable = false)
    private User user;


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)

    private String title;
    private String author;
    private String publisher;

    @Enumerated(EnumType.STRING)
    private Genre genre;

    public enum Genre {
        COMEDY,
        ROMANCE,
        THRILLER
    }
}