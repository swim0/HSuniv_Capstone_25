
package com.book_store.capstone_25.service;
import java.util.Optional;
import java.util.ArrayList;
import java.util.List;
public class BookService {

    private final List<String> books = new ArrayList<>();

    public BookService() {
        // Initialization logic
    }

    public void registerBook(String bookName) {
        if (!books.contains(bookName)) {
            books.add(bookName);
        }
    }

    public boolean removeBook(String bookName) {
        return books.remove(bookName);
    }

    public Optional<String> findBook(String bookName) {
        return books.stream().filter(book -> book.equalsIgnoreCase(bookName)).findFirst();
    }

    public boolean deleteBook(String bookName) {
        return books.removeIf(book -> book.equalsIgnoreCase(bookName));
    }
}