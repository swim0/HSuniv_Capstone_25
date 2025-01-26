
package com.book_store.capstone_25.service;
import com.book_store.capstone_25.Repository.BookRepository;
import com.book_store.capstone_25.model.Book;

import java.util.Optional;
import java.util.ArrayList;
import java.util.List;
public class BookService {

    private List<String> books = new ArrayList<>();

    private BookRepository bookRepository;
    private Book book;
    public BookService(BookRepository bookRepository, Book book) {
       this.bookRepository = bookRepository;
       this.book = book;
    }

    public Book registerBook(String bookName) {
        if (!books.contains(bookName)) {
            books.add(bookName);
        }
        return null;
    }

    public boolean removeBook(Long id) {
        return books.remove(id);
    }

    public Optional<String> findBook(String bookName) {
        return books.stream().filter(book -> book.equalsIgnoreCase(bookName)).findFirst();
    }

    public boolean deleteBook(String bookName) {
        return books.removeIf(book -> book.equalsIgnoreCase(bookName));
    }

    public Book saveBook(Book book) {
        return bookRepository.save(book);
    }



}