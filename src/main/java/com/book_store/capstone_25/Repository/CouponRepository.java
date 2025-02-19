package com.book_store.capstone_25.Repository;

import com.book_store.capstone_25.model.Coupon;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CouponRepository extends JpaRepository<Coupon, Long> {
    List<Coupon> findByUserIdAndIsUsedFalse(Long userId);
    Optional<Coupon> findCouponByCode(String code);

    List<Coupon> findByUserId(Long userId);

    Optional<Coupon> findByCodeAndUserId(String couponCode, Long userId);
}
