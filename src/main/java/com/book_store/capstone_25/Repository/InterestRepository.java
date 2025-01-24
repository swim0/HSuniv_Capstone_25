package com.book_store.capstone_25.Repository;

import com.book_store.capstone_25.model.UserInterest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
public interface InterestRepository extends JpaRepository<UserInterest, Long>{
    @Query("SELECT u from UserInterest u WHERE u.userId = :userId")
    List<UserInterest> findByUserId(@Param("userId") String userId);

    @Transactional
    @Modifying
    @Query("delete from UserInterest ui where ui.userId = :userId and ui.id = :interestId")
    void deleteByUserIdAndInterestId(@Param("userId") String userId, @Param("interestId") Long interestId);
}