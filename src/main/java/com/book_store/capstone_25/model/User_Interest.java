package com.book_store.capstone_25.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Table(name = "user_interests")
@Entity
@Getter
@Setter
public class User_Interest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Primary Key for User_Interest
    private Long id;
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false) // Foreign Key to User
    private User user;


    @Enumerated(EnumType.STRING)
    @Column(name = "genre")  // Optional: specify column name
    private Genre genre;  // Using Enum for Genre

    public enum Genre {
        풍자,
        디스토피아,
        고전,
        전후소설,
        동화
    }
}
