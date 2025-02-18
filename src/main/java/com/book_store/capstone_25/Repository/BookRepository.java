package com.book_store.capstone_25.Repository;

import com.book_store.capstone_25.model.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

// BookRepository.java
@Repository
public interface BookRepository extends JpaRepository<Book, Long> {
    List<Book> findBookByTitleContainingIgnoreCase(String title);
    List<Book> findBookByAuthorContainingIgnoreCase(String author);
    List<Book> findBookByPublisherContainingIgnoreCase(String publisher);
    List<Book> findBookByGenreContainingIgnoreCase(String genre);
}