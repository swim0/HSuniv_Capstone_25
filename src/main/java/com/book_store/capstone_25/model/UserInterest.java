package com.book_store.capstone_25.model;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jdk.jfr.Category;
import lombok.Setter;

@Entity
public class UserInterest {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String userId;

    @Setter
    @Enumerated(EnumType.STRING)
    private String Genre;

    public void setUserId(String userId) {

    }

    public void setGenre(UserInterest.Genre genre) {
    }

    public enum Genre {
        코미디,
        로맨스,
        스릴러
    }

}