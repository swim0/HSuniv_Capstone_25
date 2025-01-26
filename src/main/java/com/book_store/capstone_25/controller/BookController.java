package com.book_store.capstone_25.controller;

import com.book_store.capstone_25.DTO.BookRequest;
import com.book_store.capstone_25.Repository.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import com.book_store.capstone_25.model.Book;
import com.book_store.capstone_25.service.BookService;

@RestController
@RequestMapping("/api/")
public class BookController {

    private BookRepository bookRepository;
    private final BookService bookService;
    private final Book book_data;


    public BookController(BookService bookService,Book book_data,BookRepository bookRepository) {
        this.bookService = bookService;
        this.book_data = book_data;
        this.bookRepository = bookRepository;
    }


    @PostMapping("/books")
    public ResponseEntity<Book> createBook(@RequestBody BookRequest bookInfo) {
        Book book = new Book();
        book.setTitle(bookInfo.getTitle());
        book.setAuthor(bookInfo.getAuthor());
        book.setGenre(bookInfo.getGenre());
        book.setPublisher(bookInfo.getPublisher());
        bookService.saveBook(book); // 적절히 변경
        return ResponseEntity.ok(book);
    }

    @PostMapping("add_book")
    public ResponseEntity<Book> addBook(@RequestBody Book book) {
        Book savedBook = bookService.registerBook(String.valueOf(book));
        return ResponseEntity.ok(savedBook);
    }

    @DeleteMapping("/{book_id}")
    public ResponseEntity<Void> deleteBook(@PathVariable Long id) {
        bookService.removeBook(id);
        return ResponseEntity.noContent().build();
    }

}
