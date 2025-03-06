package com.book_store.capstone_25.Repository;

import com.book_store.capstone_25.model.SearchHistory;
import com.book_store.capstone_25.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SearchHistoryRepository extends JpaRepository<SearchHistory, Long> {

    // ✅ 특정 사용자의 전체 검색 기록 조회
    List<SearchHistory> findByUserId(Long userId);

    // ✅ 특정 사용자의 전체 검색 기록 삭제
    void deleteByUserId(Long userId);

    // ✅ 특정 사용자의 특정 검색어 삭제
    void deleteByUserIdAndKeyword(Long userId, String keyword);

    List<SearchHistory> findByUserOrderBySearchedAtDesc(User user);
}
