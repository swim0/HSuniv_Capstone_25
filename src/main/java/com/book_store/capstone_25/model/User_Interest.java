package com.book_store.capstone_25.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.util.List;

@Table(name = "user_interests")
@Entity
@Getter
@Setter
public class User_Interest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ElementCollection(targetClass = Genre.class)
    @Enumerated(EnumType.STRING)
    @CollectionTable(name = "user_interests_genres", joinColumns = @JoinColumn(name = "user_interest_id"))
    @Column(name = "genre")
    private List<Genre> genres;

    public enum Genre {
        풍자,
        디스토피아,
        고전,
        전후소설,
        동화
    }
}