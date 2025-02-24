package com.book_store.capstone_25.service;

import com.book_store.capstone_25.Repository.BookRepository;
import com.book_store.capstone_25.Repository.CommentRepository;
import com.book_store.capstone_25.Repository.UserRepository;
import com.book_store.capstone_25.model.Comment;
import com.book_store.capstone_25.model.Book;
import com.book_store.capstone_25.model.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class CommentService {
    private final CommentRepository commentRepository;
    private final BookRepository bookRepository;
    private final UserRepository userRepository;

    public CommentService(CommentRepository commentRepository, BookRepository bookRepository, UserRepository userRepository) {
        this.commentRepository = commentRepository;
        this.bookRepository = bookRepository;
        this.userRepository = userRepository;
    }


    // 특정 회원의 댓글 조회
    public List<Comment> getCommentsByUser(User user) {
        // CommentRepository에서 user에 해당하는 댓글 목록을 조회
        return commentRepository.findUserByUser((user));
    }

    public Comment addComment(Long bookId, Long userId, String content) {
        // Book과 User 객체는 미리 조회한다고 가정
        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new RuntimeException("Book not found"));
        User user = userRepository.findUsersById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        Comment comment = new Comment();
        comment.setBook(book);
        comment.setUser(user);
        comment.setContent(content);
        comment.setCreatedAt(LocalDateTime.now());
        Comment savedComment = commentRepository.save(comment);

        // 댓글 추가 후, Book의 commentCount 증가
        book.setCommentCount(book.getCommentCount() + 1);
        bookRepository.save(book);

        return savedComment;
    }

    public Optional<Comment> getCommentById(Long id) {
        return commentRepository.findById(id);
    }

    public Comment updateComment(Comment comment, String newContent) {
        comment.setContent(newContent);
        comment.setUpdatedAt(LocalDateTime.now());
        return commentRepository.save(comment);
    }

    public void deleteComment(Long commentId) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new RuntimeException("Comment not found"));
        Book book = comment.getBook();

        commentRepository.delete(comment);

        // 댓글 삭제 후, Book의 commentCount 감소
        book.setCommentCount(book.getCommentCount() - 1);
        bookRepository.save(book);
    }
}
