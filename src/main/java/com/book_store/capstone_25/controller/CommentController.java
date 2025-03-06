package com.book_store.capstone_25.controller;

import com.book_store.capstone_25.model.Book;
import com.book_store.capstone_25.model.Comment;
import com.book_store.capstone_25.model.User;
import com.book_store.capstone_25.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/comments")
public class CommentController {

    private final CommentService commentService;

    // ✅ 특정 책의 전체 댓글 조회
    @GetMapping("/book/{bookId}")
    public ResponseEntity<List<Comment>> getCommentsByBook(@PathVariable Long bookId) {
        List<Comment> comments = commentService.getCommentsByBook(bookId);
        return ResponseEntity.ok(comments);
    }

    // ✅ 특정 회원의 댓글 조회
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Comment>> getCommentsByUser(@PathVariable Long userId) {
        List<Comment> comments = commentService.getCommentsByUser(userId);
        return ResponseEntity.ok(comments);
    }

    // ✅ 댓글 추가
    @PostMapping("/{bookId}")
    public ResponseEntity<Comment> addComment(
            @PathVariable Long bookId,
            @RequestParam Long userId,
            @RequestParam String content) {
        Comment comment = commentService.addComment(bookId, userId, content);
        return ResponseEntity.ok(comment);
    }

    // ✅ 댓글 수정 (본인만 가능)
    @PutMapping("/{commentId}")
    public ResponseEntity<Comment> updateComment(
            @PathVariable Long commentId,
            @RequestParam Long userId,
            @RequestParam String newContent) {
        Comment updatedComment = commentService.updateComment(commentId, userId, newContent);
        return ResponseEntity.ok(updatedComment);
    }

    // ✅ 댓글 삭제 (본인만 가능)
    @DeleteMapping("/{commentId}")
    public ResponseEntity<String> deleteComment(@PathVariable Long commentId, @RequestParam Long userId) {
        commentService.deleteComment(commentId, userId);
        return ResponseEntity.ok("댓글이 삭제되었습니다.");
    }
}
