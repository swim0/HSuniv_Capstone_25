package com.book_store.capstone_25.service;

import com.book_store.capstone_25.Repository.BookRepository;
import com.book_store.capstone_25.Repository.CommentRepository;
import com.book_store.capstone_25.Repository.UserRepository;
import com.book_store.capstone_25.model.Comment;
import com.book_store.capstone_25.model.Book;
import com.book_store.capstone_25.model.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;
    private final BookRepository bookRepository;
    private final UserRepository userRepository;

    // ✅ 특정 회원의 댓글 조회
    public List<Comment> getCommentsByUser(Long userId) {
        return commentRepository.findCommentsByUser_id(userId);
    }

    // ✅ 특정 책의 전체 댓글 조회 (추가)
    public List<Comment> getCommentsByBook(Long bookId) {
        return commentRepository.findByBook(bookId);
    }

    // ✅ 댓글 추가
    @Transactional
    public Comment addComment(Long bookId, Long userId, String content) {
        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new RuntimeException("Book not found"));
        User user = userRepository.findUsersById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Comment comment = Comment.builder()
                .book(book)
                .user(user)
                .content(content)
                .createdAt(LocalDateTime.now())
                .build();

        Comment savedComment = commentRepository.save(comment);

        // 댓글 추가 후, Book의 commentCount 증가
        book.setCommentCount(book.getCommentCount() + 1);
        bookRepository.save(book);

        return savedComment;
    }

    // ✅ 댓글 ID로 조회
    public Comment getCommentById(Long id) {
        return commentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Comment not found"));
    }

    // ✅ 댓글 수정
    @Transactional
    public Comment updateComment(Long commentId, Long userId, String newContent) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new RuntimeException("Comment not found"));

        // 작성자 본인만 수정 가능
        if (!comment.getUser().getId().equals(userId)) {
            throw new RuntimeException("댓글을 수정할 권한이 없습니다.");
        }

        comment.setContent(newContent);
        comment.setUpdatedAt(LocalDateTime.now());
        return commentRepository.save(comment);
    }

    // ✅ 댓글 삭제 (작성자 본인만 가능)
    @Transactional
    public void deleteComment(Long commentId, Long userId) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new RuntimeException("Comment not found"));

        // 작성자 본인만 삭제 가능
        if (!comment.getUser().getId().equals(userId)) {
            throw new RuntimeException("댓글을 삭제할 권한이 없습니다.");
        }

        Book book = comment.getBook();
        commentRepository.delete(comment);

        // 댓글 삭제 후, Book의 commentCount 감소
        book.setCommentCount(book.getCommentCount() - 1);
        bookRepository.save(book);
    }
}
