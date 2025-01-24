package com.book_store.capstone_25.model;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Setter;

@Entity
public class UserInterest {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Setter

    private String userId;

    @Setter
    @Enumerated(EnumType.STRING)
    private Genre genre;

    public enum Genre {
        COMEDY,
        ROMANCE,
        THRILLER
    }

}