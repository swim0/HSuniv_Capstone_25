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


    public List<Book> searchBooks(String title, String author,String publisher, String genre) {
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
