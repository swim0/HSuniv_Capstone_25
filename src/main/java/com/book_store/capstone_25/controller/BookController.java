package com.book_store.capstone_25.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import com.book_store.capstone_25.model.Book;
import com.book_store.capstone_25.service.BookService;

@RestController
@RequestMapping("/api/")
public class BookController {

    @Autowired
    private BookService bookService;

    @PostMapping("/books")
    public ResponseEntity<java.awt.print.Book> createBook(Account account, @RequestBody BookDto bookDto) {
        java.awt.print.Book book = new java.awt.print.Book(bookDto.getTitle(), bookDto.getAuthor(), bookDto.getGenre(), bookDto.getPublisher());
        bookService.save(book);
        return ResponseEntity.ok(book);
    }

    @PostMapping("/add_book")
    public ResponseEntity<Book> addBook(@RequestBody Book book) {
        Book savedBook = bookService.saveBook(book);
        return ResponseEntity.ok(savedBook);
    }

    @DeleteMapping("/{book_id}")
    public ResponseEntity<Void> deleteBook(@PathVariable Long id) {
        bookService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

}
