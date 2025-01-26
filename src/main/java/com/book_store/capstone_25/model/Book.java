package com.book_store.capstone_25.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
public class Book {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String author;

    private String genre;

    @Column(nullable = false)
    private String publisher;

    public String setTitle(String title){
       return this.title = title;
    };

    public String setAuthor(String author){
        return this.author = author;
    };

    public String setGenre(String genre){
        return this.genre = genre;
    };

    public String setPublisher(String publisher){
        return this.publisher = publisher;
    };





}