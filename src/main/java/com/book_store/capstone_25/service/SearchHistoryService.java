package com.book_store.capstone_25.service;

import com.book_store.capstone_25.Repository.SearchHistoryRepository;
import com.book_store.capstone_25.model.SearchHistory;
import com.book_store.capstone_25.model.User;
import com.book_store.capstone_25.Repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class SearchHistoryService {

    private final SearchHistoryRepository searchHistoryRepository;
    private final UserRepository userRepository;

    // ✅ 특정 사용자의 검색 기록 조회
    public List<SearchHistory> getSearchHistory(Long userId) {
        return searchHistoryRepository.findByUserId(userId);
    }

    // ✅ 검색 기록 추가
    public void addSearchHistory(Long userId, String keyword) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다."));

        SearchHistory searchHistory = SearchHistory.builder()
                .user(user)
                .keyword(keyword)
                .searchedAt(LocalDateTime.now())
                .build();

        searchHistoryRepository.save(searchHistory);
    }

    // ✅ 검색 기록 전체 삭제
    @Transactional
    public void deleteAllSearchHistory(Long userId) {
        searchHistoryRepository.deleteByUserId(userId);
    }

    // ✅ 특정 키워드 검색 기록 삭제
    @Transactional
    public void deleteSearchHistoryByKeyword(Long userId, String keyword) {
        searchHistoryRepository.deleteByUserIdAndKeyword(userId, keyword);
    }
}
