package com.book_store.capstone_25.controller;

import com.book_store.capstone_25.DTO.BookDTO;
import com.book_store.capstone_25.Repository.BookRepository;
import com.book_store.capstone_25.Repository.SearchHistoryRepository;
import com.book_store.capstone_25.Repository.UserRepository;
import com.book_store.capstone_25.model.Book;
import com.book_store.capstone_25.model.SearchHistory;
import com.book_store.capstone_25.model.User;
import com.book_store.capstone_25.service.BookService;
import com.book_store.capstone_25.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
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
@RequestMapping("/api/books") // 도서관련 API 전체 경로
@RequiredArgsConstructor
public class BookController {

    private final BookRepository bookRepository;
    private final BookService bookService;
    private final UserRepository userRepository;
    private final SearchHistoryRepository searchHistoryRepository;

    // 책 정보 저장 (이미지 포함) 이미지 경로 src/main/java/resource/static/images
    @PostMapping("/product")
    public ResponseEntity<Book> createBook(@ModelAttribute BookDTO bookDTO) throws IOException {
        Book book = new Book();
        book.setTitle(bookDTO.getTitle());
        book.setAuthor(bookDTO.getAuthor());
        book.setPublisher(bookDTO.getPublisher());
        book.setGenre(bookDTO.getGenre());
        book.setPrice(bookDTO.getPrice());

        // ✅ 이미지 저장 처리 (JAR 외부 디렉토리)
        if (bookDTO.getImage() != null && !bookDTO.getImage().isEmpty()) {
            String fileName = UUID.randomUUID() + "_" + bookDTO.getImage().getOriginalFilename();

            // ✅ 실제 서버의 이미지 디렉토리 경로
            String uploadDir = "/home/dayzen32/images/";
            Path filePath = Paths.get(uploadDir + fileName);
            Files.write(filePath, bookDTO.getImage().getBytes());

            // ✅ URL에 접근할 경로는 "/images/파일명"
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

    @GetMapping("/add_book6")
    public ResponseEntity<Book> add_book6() {
        // 이미지 경로를 설정 (static 폴더 내 파일을 직접 사용)
        String imagePath = "/images/gundal.jpg";

        Book book = bookService.createBookFromResource(
                "낭만 건달",
                "요제프 폰 아이헨도르프",
                "교보문고",
                "고전",
                new BigDecimal("15000"),
                imagePath
        );

        return ResponseEntity.ok(book);
    }

    @GetMapping("/add_book7")
    public ResponseEntity<Book> add_book7() {
        // 이미지 경로를 설정 (static 폴더 내 파일을 직접 사용)
        String imagePath = "/images/buin.jpg";

        Book book = bookService.createBookFromResource(
                "보바리 부인",
                "귀스타브 플로베르",
                "문예출판사",
                "고전",
                new BigDecimal("10800"),
                imagePath
        );

        return ResponseEntity.ok(book);
    }

    @GetMapping("/add_book8")
    public ResponseEntity<Book> add_book8() {
        // 이미지 경로를 설정 (static 폴더 내 파일을 직접 사용)
        String imagePath = "/images/pasang.jpg";

        Book book = bookService.createBookFromResource(
                "모파상 단편선",
                "기 드 모파상",
                "문예출판사",
                "고전",
                new BigDecimal("7200"),
                imagePath
        );

        return ResponseEntity.ok(book);
    }

    @GetMapping("/add_book9")
    public ResponseEntity<Book> add_book9() {
        // 이미지 경로를 설정 (static 폴더 내 파일을 직접 사용)
        String imagePath = "/images/woman.jpg";

        Book book = bookService.createBookFromResource(
                "위기의 여자",
                "시몬 드 보부아르",
                "문예출판사",
                "고전",
                new BigDecimal("9900"),
                imagePath
        );

        return ResponseEntity.ok(book);
    }

    @GetMapping("/add_book10")
    public ResponseEntity<Book> add_book10() {
        // 이미지 경로를 설정 (static 폴더 내 파일을 직접 사용)
        String imagePath = "/images/star.jpg";

        Book book = bookService.createBookFromResource(
                "별에게",
                "안녕달",
                "창비",
                "동화",
                new BigDecimal("15120"),
                imagePath
        );

        return ResponseEntity.ok(book);
    }

    @GetMapping("/add_book11")
    public ResponseEntity<Book> add_book11() {
        // 이미지 경로를 설정 (static 폴더 내 파일을 직접 사용)
        String imagePath = "/images/happy.jpg";

        Book book = bookService.createBookFromResource(
                "해피버쓰데이",
                "백희나",
                "스토리보울",
                "동화",
                new BigDecimal("15300"),
                imagePath
        );

        return ResponseEntity.ok(book);
    }

    @GetMapping("/add_book12")
    public ResponseEntity<Book> add_book12() {
        // 이미지 경로를 설정 (static 폴더 내 파일을 직접 사용)
        String imagePath = "/images/you.jpg";

        Book book = bookService.createBookFromResource(
                "너였구나",
                "이석훈",
                "창비교육",
                "동화",
                new BigDecimal("15200"),
                imagePath
        );

        return ResponseEntity.ok(book);
    }

    @GetMapping("/add_book13")
    public ResponseEntity<Book> add_book13() {
        // 이미지 경로를 설정 (static 폴더 내 파일을 직접 사용)
        String imagePath = "/images/reason.jpg";

        Book book = bookService.createBookFromResource(
                "라임 앤 리즌 1: 디스토피아",
                "예소연",
                "김영사",
                "디스토피아",
                new BigDecimal("13200"),
                imagePath
        );

        return ResponseEntity.ok(book);
    }

    @GetMapping("/add_book14")
    public ResponseEntity<Book> add_book14() {
        // 이미지 경로를 설정 (static 폴더 내 파일을 직접 사용)
        String imagePath = "/images/life.jpg";

        Book book = bookService.createBookFromResource(
                "핵전쟁 디스토피아 생존기",
                "기장",
                "아르텐",
                "디스토피아",
                new BigDecimal("13200"),
                imagePath
        );

        return ResponseEntity.ok(book);
    }

    @GetMapping("/add_book15")
    public ResponseEntity<Book> add_book15() {
        // 이미지 경로를 설정 (static 폴더 내 파일을 직접 사용)
        String imagePath = "/images/human.jpg";

        Book book = bookService.createBookFromResource(
                "잉여인간",
                "손창섭",
                "민음사",
                "전후소설",
                new BigDecimal("13200"),
                imagePath
        );

        return ResponseEntity.ok(book);
    }

    @GetMapping("/add_book16")
    public ResponseEntity<Book> add_book16() {
        // 이미지 경로를 설정 (static 폴더 내 파일을 직접 사용)
        String imagePath = "/images/Ewha.jpg";

        Book book = bookService.createBookFromResource(
                "수난 이대",
                "전국국어교사모임",
                "휴머니스트",
                "전후소설",
                new BigDecimal("14000"),
                imagePath
        );

        return ResponseEntity.ok(book);
    }

    @GetMapping("/add_book17")
    public ResponseEntity<Book> add_book17() {
        // 이미지 경로를 설정 (static 폴더 내 파일을 직접 사용)
        String imagePath = "/images/gene.jpg";

        Book book = bookService.createBookFromResource(
                "불신시대",
                "박경리",
                "문학과지성사",
                "전후소설",
                new BigDecimal("10800"),
                imagePath
        );

        return ResponseEntity.ok(book);
    }

    @GetMapping("/add_book18")
    public ResponseEntity<Book> add_book18() {
        // 이미지 경로를 설정 (static 폴더 내 파일을 직접 사용)
        String imagePath = "/images/hw.jpg";

        Book book = bookService.createBookFromResource(
                "풍자화전",
                "제아미",
                "지만지드라마",
                "풍자",
                new BigDecimal("15120"),
                imagePath
        );

        return ResponseEntity.ok(book);
    }

    @GetMapping("/add_book19")
    public ResponseEntity<Book> add_book19() {
        // 이미지 경로를 설정 (static 폴더 내 파일을 직접 사용)
        String imagePath = "/images/pung.jpg";

        Book book = bookService.createBookFromResource(
                "권력과 풍자",
                "류재화",
                "한길아트",
                "풍자",
                new BigDecimal("12200"),
                imagePath
        );

        return ResponseEntity.ok(book);
    }


    @GetMapping("/add_book20")
    public ResponseEntity<Book> add_book20() {
        // 이미지 경로를 설정 (static 폴더 내 파일을 직접 사용)
        String imagePath = "/images/cheir.jpg";

        Book book = bookService.createBookFromResource(
                "풍자, 시인의 의자",
                "김관식",
                "이바구",
                "풍자",
                new BigDecimal("13500"),
                imagePath
        );

        return ResponseEntity.ok(book);
    }
    // 특정 책 조회
    @GetMapping("/goods/{id}")
    public ResponseEntity<Book> getBook(@PathVariable Long id) {
        return bookRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/delete_book_by_title")
    public ResponseEntity<String> deleteBookByTitle(@RequestParam String title) {
        List<Book> books = bookRepository.findBookByTitle(title);
        if (books.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("해당 제목의 책이 없습니다.");
        }

        bookRepository.deleteAll(books);
        return ResponseEntity.ok("삭제 완료: " + title);
    }

    // 전체 책 목록 조회
    @GetMapping
    public ResponseEntity<List<Book>> getAllBooks() {
        return ResponseEntity.ok(bookRepository.findAll());
    }

    // 검색 기능
    @GetMapping("/search")
    public ResponseEntity<List<Book>> searchBooks(
            @RequestParam(required = false) Long userId, //  userId가 선택적 (Optional) -> 로그인 시에만 검색기록 수집
            @RequestParam(required = false) String title,
            @RequestParam(required = false) String author,
            @RequestParam(required = false) String publisher,
            @RequestParam(required = false) String genre) {
        return ResponseEntity.ok(bookService.searchBooks(userId, title, author, publisher, genre));
    }
    // 특정 유저 검색기록 조회
    @GetMapping("/search/history")
    public ResponseEntity<List<SearchHistory>> getUserSearchHistory(@RequestParam Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다."));
        return ResponseEntity.ok(searchHistoryRepository.findByUserOrderBySearchedAtDesc(user));
    }
}
