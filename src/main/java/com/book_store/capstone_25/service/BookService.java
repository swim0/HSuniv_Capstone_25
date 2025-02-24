package com.book_store.capstone_25.service;

import com.book_store.capstone_25.Repository.BookRepository;
import com.book_store.capstone_25.Repository.SearchHistoryRepository;
import com.book_store.capstone_25.Repository.UserRepository;
import com.book_store.capstone_25.model.Book;
import com.book_store.capstone_25.model.SearchHistory;
import com.book_store.capstone_25.model.User;
import jakarta.annotation.Resource;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;
import org.springframework.util.StreamUtils;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
public class BookService {
    public final BookRepository bookRepository;
    public final UserRepository userRepository;
    public final SearchHistoryRepository searchHistoryRepository;

    public BookService(BookRepository bookRepository, UserRepository userRepository, SearchHistoryRepository searchHistoryRepository) {
        this.bookRepository = bookRepository;
        this.userRepository = userRepository;
        this.searchHistoryRepository = searchHistoryRepository;
    }

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
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다."));

        // 🔹 검색 기록 저장
        SearchHistory searchHistory = new SearchHistory(user, title, author, publisher, genre);
        searchHistoryRepository.save(searchHistory);

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

}
