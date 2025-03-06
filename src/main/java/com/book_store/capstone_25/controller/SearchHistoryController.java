package com.book_store.capstone_25.controller;

import com.book_store.capstone_25.model.SearchHistory;
import com.book_store.capstone_25.service.SearchHistoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/search-history")
public class SearchHistoryController {

    private final SearchHistoryService searchHistoryService;

    // ✅ 특정 사용자의 검색 기록 조회
    @GetMapping("/{userId}")
    public ResponseEntity<List<SearchHistory>> getSearchHistory(@PathVariable Long userId) {
        return ResponseEntity.ok(searchHistoryService.getSearchHistory(userId));
    }

    // ✅ 검색 기록 추가 (테스트용 API)
    @PostMapping("/{userId}")
    public ResponseEntity<String> addSearchHistory(@PathVariable Long userId, @RequestParam String keyword) {
        searchHistoryService.addSearchHistory(userId, keyword);
        return ResponseEntity.ok("검색 기록이 추가되었습니다.");
    }

    // ✅ 검색 기록 전체 삭제
    @DeleteMapping("/{userId}")
    public ResponseEntity<String> deleteAllSearchHistory(@PathVariable Long userId) {
        searchHistoryService.deleteAllSearchHistory(userId);
        return ResponseEntity.ok("전체 검색 기록이 삭제되었습니다.");
    }

    // ✅ 특정 키워드 검색 기록 삭제
    @DeleteMapping("/{userId}/keyword")
    public ResponseEntity<String> deleteSearchHistoryByKeyword(@PathVariable Long userId, @RequestParam String keyword) {
        searchHistoryService.deleteSearchHistoryByKeyword(userId, keyword);
        return ResponseEntity.ok("검색 기록이 삭제되었습니다.");
    }
}
