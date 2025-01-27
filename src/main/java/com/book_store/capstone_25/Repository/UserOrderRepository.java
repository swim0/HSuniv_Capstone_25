package com.book_store.capstone_25.Repository;

import com.book_store.capstone_25.model.User;
import com.book_store.capstone_25.model.User_order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;


public interface UserOrderRepository extends JpaRepository<User_order, Long> {


}