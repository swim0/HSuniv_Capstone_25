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
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.UUID;

// BookController.java
@RestController
@RequestMapping("/api/books")
@RequiredArgsConstructor
public class BookController {

    private final BookRepository bookRepository;
    private final BookService bookService;


    // 책 정보 저장 (이미지 포함) 이미지 경로 src/main/java/resource/static/images
    @PostMapping("/product")
    public ResponseEntity<Book> createBook(@ModelAttribute BookDTO bookDTO) throws IOException {
        Book book = new Book();
        book.setTitle(bookDTO.getTitle());
        book.setAuthor(bookDTO.getAuthor());
        book.setPublisher(bookDTO.getPublisher());
        book.setGenre(bookDTO.getGenre());
        book.setPrice(bookDTO.getPrice());

        // 이미지 저장 처리
        if (bookDTO.getImage() != null && !bookDTO.getImage().isEmpty()) {
            String fileName = UUID.randomUUID() + "_" + bookDTO.getImage().getOriginalFilename();
            Path filePath = Paths.get("src/main/resources/static/images/" + fileName);
            Files.write(filePath, bookDTO.getImage().getBytes());

            // 저장된 이미지 경로를 DB에 저장
            book.setImageUrl("/images/" + fileName);
        }

        return ResponseEntity.ok(bookRepository.save(book));
    }

    // add_books는 백엔드 테스트용도! 프론트 분들도 참고하세요
    @GetMapping("/add_books")
    public ResponseEntity<Book> registerBook() {
        // 이미지 경로를 설정 (static 폴더 내 파일을 직접 사용)
        String imagePath = "/images/littleprince.jpg";

        Book book = bookService.createBookFromResource(
                "어린왕자",
                "생택쥐페리",
                "문학동네",
                "동화",
                new BigDecimal("12000"),
                imagePath
        );

        return ResponseEntity.ok(book);
    }

    @GetMapping("/add_book2")
    public ResponseEntity<Book> add_book2() {
        // 이미지 경로를 설정 (static 폴더 내 파일을 직접 사용)
        String imagePath = "/images/barsker.jpg";

        Book book = bookService.createBookFromResource(
                "바스커빌가의 사냥개",
                "아서 코난 도일",
                "민음사",
                "고전",
                new BigDecimal("11340"),
                imagePath
        );

        return ResponseEntity.ok(book);
    }

    @GetMapping("/add_book3")
    public ResponseEntity<Book> add_book3() {
        // 이미지 경로를 설정 (static 폴더 내 파일을 직접 사용)
        String imagePath = "/images/kr.jpg";

        Book book = bookService.createBookFromResource(
                "한국 전후소설 연구",
                "최예열",
                "역락",
                "전후소설",
                new BigDecimal("9500"),
                imagePath
        );

        return ResponseEntity.ok(book);
    }

    @GetMapping("/add_book4")
    public ResponseEntity<Book> add_book4() {
        // 이미지 경로를 설정 (static 폴더 내 파일을 직접 사용)
        String imagePath = "/images/Animal_Farm.jpg";

        Book book = bookService.createBookFromResource(
                "동물농장",
                "조지오웰",
                "교보문고",
                "풍자",
                new BigDecimal("7200"),
                imagePath
        );

        return ResponseEntity.ok(book);
    }

    @GetMapping("/add_book5")
    public ResponseEntity<Book> add_book5() {
        // 이미지 경로를 설정 (static 폴더 내 파일을 직접 사용)
        String imagePath = "/images/city.jpg";

        Book book = bookService.createBookFromResource(
                "눈먼 자들의 도시",
                "사리마구",
                "교보문고",
                "디스토피아",
                new BigDecimal("13200"),
                imagePath
        );

        return ResponseEntity.ok(book);
    }




    // 전체 책 목록 조회
    @GetMapping
    public ResponseEntity<List<Book>> getAllBooks() {
        return ResponseEntity.ok(bookRepository.findAll());
    }


    // 검색 기능
    @GetMapping("/search")
    public ResponseEntity<List<Book>> searchBooks(
            @RequestParam(required = false) String title,
            @RequestParam(required = false) String author,
            @RequestParam(required = false) String publishe,
            @RequestParam(required = false) String genre) {
        return ResponseEntity.ok(bookService.searchBooks(title, author,publishe, genre));
    }
}
