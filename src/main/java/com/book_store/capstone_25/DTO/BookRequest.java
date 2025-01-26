package com.book_store.capstone_25.DTO;

import com.book_store.capstone_25.controller.BookController;
import lombok.Data;
import lombok.Getter;
import com.book_store.capstone_25.Repository.BookRepository;
import com.book_store.capstone_25.model.Book;
import com.book_store.capstone_25.service.BookService;

public class BookRequest {

    private Long id;
    private String title;
    private String author;
    private String genre;
    private String publisher;

    private BookRepository bookRepository;
    private Book book;
    private BookService bookService;

    public BookRequest(Book book,BookRepository bookRepository, BookService bookService){
        this.book = book;
        this.bookRepository = bookRepository;
        this.bookService = bookService;

    }

    public String getTitle() {
        return book.setTitle(title);
    }

    public String getAuthor() {
        return book.setAuthor(author);
    }

    public String getGenre() {
        return book.setGenre(genre);
    }

    public String getPublisher() {
        return book.setPublisher(publisher);
    }

}
