package com.book_store.capstone_25.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Table (name = "interests")
@Entity
@Getter
@Setter
public class UserInterest {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "interest_no", nullable = false)  // 외래 키 컬럼명을 user_id로 변경
    private User user;  //

    private String userId;

    @Enumerated(EnumType.STRING)
    private Genre genre;  // String이 아닌 Genre enum 타입으로 변경

    public enum Genre {
        ROMANCE,
        COMEDY,
        THRILLER
    }
}