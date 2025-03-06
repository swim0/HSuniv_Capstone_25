package com.book_store.capstone_25.Repository;

import com.book_store.capstone_25.model.Book;
import com.book_store.capstone_25.model.Comment;
import com.book_store.capstone_25.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    List<Comment> findByBook(Book book);

    List<Comment> findUserByUser(User user);
}
