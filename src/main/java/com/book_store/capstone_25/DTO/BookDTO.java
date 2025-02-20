package com.book_store.capstone_25.DTO;

import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;

// BookDTO.java
@Getter @Setter
public class BookDTO {
    private Long bookId;
    private String title;
    private String author;
    private String publisher;
    private String genre;
    private BigDecimal price;
    private MultipartFile image;  // 이미지 파일 업로드를 위한 필드
}