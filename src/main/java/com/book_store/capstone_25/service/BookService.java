package com.book_store.capstone_25.service;

import com.book_store.capstone_25.Repository.BookRepository;
import com.book_store.capstone_25.model.Book;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class BookService {

    private final BookRepository bookRepository;

    public BookService(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    public Book registerBook(Book book) {
        return bookRepository.save(book);
    }

    public boolean removeBook(Long id) {
        if (bookRepository.existsById(id)) {
            bookRepository.deleteById(id);
            return true;
        }
        return false;
    }

    public Optional<Book> findBook(Long id) {
        return bookRepository.findById(id);
    }

    public boolean deleteBook(Long id) {
        if (bookRepository.existsById(id)) {
            bookRepository.deleteById(id);
            return true;
        }
        return false;
    }

    public List<Book> getAllBooks() {
        return bookRepository.findAll();
    }
}
