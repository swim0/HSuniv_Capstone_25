package com.book_store.capstone_25.DTO;

import com.book_store.capstone_25.model.Book.Genre;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BookRequest {
    private Long id;        // 책 ID
    private String title;   // 책 제목
    private String author;  // 저자
    private Genre genre;    // 장르
    private String publisher; // 출판사
}
