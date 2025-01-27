package com.book_store.capstone_25.service;

import com.book_store.capstone_25.Repository.UserOrderRepository;
import com.book_store.capstone_25.model.User_order;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserOrderService {

    private final UserOrderRepository userOrderRepository;

    @Autowired
    public UserOrderService(UserOrderRepository userOrderRepository) {
        this.userOrderRepository = userOrderRepository;
    }


}