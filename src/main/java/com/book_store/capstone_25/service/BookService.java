package com.book_store.capstone_25.service;

import com.book_store.capstone_25.Repository.BookRepository;
import com.book_store.capstone_25.Repository.SearchHistoryRepository;
import com.book_store.capstone_25.Repository.UserRepository;
import com.book_store.capstone_25.model.Book;
import com.book_store.capstone_25.model.SearchHistory;
import com.book_store.capstone_25.model.User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
public class BookService {
    private final BookRepository bookRepository;
    private final UserRepository userRepository;
    private final SearchHistoryRepository searchHistoryRepository;

    public BookService(BookRepository bookRepository, UserRepository userRepository, SearchHistoryRepository searchHistoryRepository) {
        this.bookRepository = bookRepository;
        this.userRepository = userRepository;
        this.searchHistoryRepository = searchHistoryRepository;
    }

    // ✅ 도서 등록 (이미지 포함)
    public Book createBookFromResource(String title, String author, String publisher,
                                       String genre, BigDecimal price, String resourcePath) {
        Book book = new Book();
        book.setTitle(title);
        book.setAuthor(author);
        book.setPublisher(publisher);
        book.setGenre(genre);
        book.setPrice(price);

        // 이미지 경로 저장 (예: "/images/littleprince.jpg")
        book.setImageUrl("/" + resourcePath);

        return bookRepository.save(book);
    }

    public List<Book> searchBooks(Long userId, String title, String author, String publisher, String genre) {
        if (userId != null) {
            User user = userRepository.findById(userId)
                    .orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다."));

            // 🔹 기존 생성자 방식 대신 빌더 패턴 사용
            SearchHistory searchHistory = SearchHistory.builder()
                    .user(user)
                    .keyword(title != null ? title : "")  // 🔹 title을 keyword로 저장 (기본값: 빈 문자열)
                    .title(title)
                    .author(author)
                    .publisher(publisher)
                    .genre(genre)
                    .build();

            searchHistoryRepository.save(searchHistory);
        }

        // 🔹 도서 검색 로직
        if (title != null && !title.isEmpty()) {
            return bookRepository.findBookByTitleContainingIgnoreCase(title);
        } else if (author != null && !author.isEmpty()) {
            return bookRepository.findBookByAuthorContainingIgnoreCase(author);
        } else if (publisher != null && !publisher.isEmpty()) {
            return bookRepository.findBookByPublisherContainingIgnoreCase(publisher);
        } else if (genre != null && !genre.isEmpty()) {
            return bookRepository.findBookByGenreContainingIgnoreCase(genre);
        }

        return bookRepository.findAll();
    }


    // ✅ 검색 기록 전체 삭제 (사용자별)
    @Transactional
    public void deleteAllSearchHistory(Long userId) {
        searchHistoryRepository.deleteByUserId(userId);
    }

    // ✅ 특정 키워드 검색 기록 삭제 (사용자별)
    @Transactional
    public void deleteSearchHistoryByKeyword(Long userId, String keyword) {
        searchHistoryRepository.deleteByUserIdAndKeyword(userId, keyword);
    }
}
