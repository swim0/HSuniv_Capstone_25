package com.book_store.capstone_25.Repository;

import com.book_store.capstone_25.model.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

// BookRepository.java
@Repository
public interface BookRepository extends JpaRepository<Book, Long> {
}