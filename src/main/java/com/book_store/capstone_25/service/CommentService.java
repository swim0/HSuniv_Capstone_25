package com.book_store.capstone_25.service;

import com.book_store.capstone_25.Repository.CommentRepository;
import com.book_store.capstone_25.model.Comment;
import com.book_store.capstone_25.model.Book;
import com.book_store.capstone_25.model.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CommentService {
    private final CommentRepository commentRepository;

    // 특정 회원의 댓글 조회
    public List<Comment> getCommentsByUser(User user) {
        // CommentRepository에서 user에 해당하는 댓글 목록을 조회
        return commentRepository.findUserByUser((user));
    }

    public Comment addComment(Book book, User user, String content) {
        Comment comment = new Comment();
        comment.setBook(book);
        comment.setUser(user);
        comment.setContent(content);
        return commentRepository.save(comment);
    }

    public Optional<Comment> getCommentById(Long id) {
        return commentRepository.findById(id);
    }

    public Comment updateComment(Comment comment, String newContent) {
        comment.setContent(newContent);
        comment.setUpdatedAt(LocalDateTime.now());
        return commentRepository.save(comment);
    }

    public void deleteComment(Long id) {
        commentRepository.deleteById(id);
    }
}
