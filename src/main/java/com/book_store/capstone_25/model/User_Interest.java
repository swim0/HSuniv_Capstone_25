package com.book_store.capstone_25.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Table (name = "user_interests")
@Entity
@Getter
@Setter
public class User_Interest {
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)  // 외래 키 컬럼명을 user_id로 변경
    private User user;  //

    @GeneratedValue
    @Id
    @Column(name = "user_id", insertable = false, updatable = false)
    private String user_id;
    @Enumerated(EnumType.STRING)
    private Genre genre;  // String이 아닌 Genre enum 타입으로 변경

    public enum Genre {
        풍자,
        디스토피아,
        고전,
        전후소설,
        동화
    }
}