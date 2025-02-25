package com.book_store.capstone_25.controller;

import com.book_store.capstone_25.Repository.CommentRepository;
import com.book_store.capstone_25.model.Book;
import com.book_store.capstone_25.model.Comment;
import com.book_store.capstone_25.model.User;
import com.book_store.capstone_25.Repository.BookRepository;
import com.book_store.capstone_25.Repository.UserRepository;
import com.book_store.capstone_25.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/comments") // 댓글 API에 대한 전체경로
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;
    private final BookRepository bookRepository;
    private final UserRepository userRepository;



    // 특정 회원의 댓글 조회
    @GetMapping("/user/{userId}/comments")
    public ResponseEntity<List<Comment>> getCommentsByUser(@PathVariable Long userId) {
        Optional<User> user = userRepository.findUsersById(userId);
        if (user.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        List<Comment> comments = commentService.getCommentsByUser(user.get());
        return ResponseEntity.ok(comments);
    }
    // 댓글 추가
    @PostMapping("/book/{bookId}/user/{userId}")
    public ResponseEntity<Comment> addComment(@PathVariable Long bookId,
                                              @PathVariable Long userId,
                                              @RequestParam String content) {
        Optional<Book> book = bookRepository.findBookByBookId(bookId);
        Optional<User> user = userRepository.findUsersById(userId);

        if (book.isEmpty() || user.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        Comment savedComment = commentService.addComment(book.get().getBookId(), user.get().getId(), content);
        return ResponseEntity.ok(savedComment);
    }

    // 댓글 수정
    @PutMapping("/{commentId}")
    public ResponseEntity<Comment> updateComment(@PathVariable Long commentId,
                                                 @RequestParam String newContent) {
        Optional<Comment> commentOpt = commentService.getCommentById(commentId);
        if (commentOpt.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        Comment updatedComment = commentService.updateComment(commentOpt.get(), newContent);
        return ResponseEntity.ok(updatedComment);
    }

    // 댓글 삭제
    @DeleteMapping("/{commentId}")
    public ResponseEntity<?> deleteComment(@PathVariable Long commentId) {
        commentService.deleteComment(commentId);
        return ResponseEntity.ok().build();
    }
}
