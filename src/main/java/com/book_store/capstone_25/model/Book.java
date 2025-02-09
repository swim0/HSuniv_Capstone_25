package com.book_store.capstone_25.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

// Book.java
@Entity
@Table(name = "books")
@Getter @Setter
public class Book {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long bookId;

    private String title;
    private String author;
    private String publisher;
    private String genre;
    private BigDecimal price;

    @Lob
    private byte[] bookImage;
    private String imageType;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}




