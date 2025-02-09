package com.book_store.capstone_25.controller;

import com.book_store.capstone_25.DTO.BookDTO;
import com.book_store.capstone_25.Repository.BookRepository;
import com.book_store.capstone_25.model.Book;
import com.book_store.capstone_25.service.BookService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;

// BookController.java
@RestController
@RequestMapping("/api/books")
@RequiredArgsConstructor
public class BookController {

    private final BookRepository bookRepository;
    private final BookService bookService;
    // 책 정보 저장 (이미지 포함)
    @PostMapping("/product")
    public ResponseEntity<Book> createBook(@ModelAttribute BookDTO bookDTO) throws IOException {
        Book book = new Book();
        book.setTitle(bookDTO.getTitle());
        book.setAuthor(bookDTO.getAuthor());
        book.setPublisher(bookDTO.getPublisher());
        book.setGenre(bookDTO.getGenre());
        book.setPrice(bookDTO.getPrice());

        // 이미지 처리
        if (bookDTO.getImage() != null && !bookDTO.getImage().isEmpty()) {
            book.setBookImage(bookDTO.getImage().getBytes());
            book.setImageType(bookDTO.getImage().getContentType());
        }

        return ResponseEntity.ok(bookRepository.save(book));
    }

    // add_books는 백엔드 테스트용도 프론트 분들은 신경쓰지마세요
    @GetMapping("/add_books")
    public ResponseEntity<Book> registerBook() {
        Book book = bookService.createBookFromResource(
                "어린왕자",
                "생텍쥐페리",
                "문학동네",
                "소설",
                new BigDecimal("12000"),
                "static/images/littleprince.jpg"
        );
        return ResponseEntity.ok(book);
    }

    // 이미지 조회
    @GetMapping("/{id}/image")
    public ResponseEntity<byte[]> getBookImage(@PathVariable Long id) {
        Book book = bookRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Book not found"));

        if (book.getBookImage() == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(book.getImageType()))
                .body(book.getBookImage());
    }

    // 전체 책 목록 조회
    @GetMapping
    public ResponseEntity<List<Book>> getAllBooks() {
        return ResponseEntity.ok(bookRepository.findAll());
    }

    // 특정 책 조회
    @GetMapping("/goods/{id}")
    public ResponseEntity<Book> getBook(@PathVariable Long id) {
        return bookRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}
