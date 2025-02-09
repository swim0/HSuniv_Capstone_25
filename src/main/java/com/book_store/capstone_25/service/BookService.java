package com.book_store.capstone_25.service;

import com.book_store.capstone_25.Repository.BookRepository;
import com.book_store.capstone_25.model.Book;
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

    public BookService(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    public Book createBookFromResource(String title, String author, String publisher,
                                       String genre, BigDecimal price, String resourcePath) {
        try {
            // 클래스패스의 리소스에서 이미지 읽기
            ClassPathResource resource = new ClassPathResource(resourcePath);
            byte[] imageBytes = StreamUtils.copyToByteArray(((ClassPathResource) resource).getInputStream());

            Book book = new Book();
            book.setTitle(title);
            book.setAuthor(author);
            book.setPublisher(publisher);
            book.setGenre(genre);
            book.setPrice(price);
            book.setBookImage(imageBytes);
            book.setImageType("image/jpeg"); // 또는 적절한 타입
            return bookRepository.save(book);
        } catch (IOException e) {
            throw new RuntimeException("이미지 저장 실패", e);
        }
    }
}
