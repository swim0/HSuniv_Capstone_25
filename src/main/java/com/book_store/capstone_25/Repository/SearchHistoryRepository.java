package com.book_store.capstone_25.Repository;

import com.book_store.capstone_25.model.SearchHistory;
import com.book_store.capstone_25.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SearchHistoryRepository extends JpaRepository<SearchHistory, Long> {
    List<SearchHistory> findByUserOrderBySearchedAtDesc(User user);
}
