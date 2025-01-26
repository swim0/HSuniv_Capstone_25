package com.book_store.capstone_25.DTO;


import com.book_store.capstone_25.service.UserService;
import lombok.Data;
import lombok.Getter;
import com.book_store.capstone_25.Repository.UserRepository;
import com.book_store.capstone_25.model.User;

import java.util.Optional;

@Getter
@Data
public class LoginRequest {
    public String userId;
    public String password;
    private final UserService userService;
    private final UserRepository userRepository;
    private final User user;

    public LoginRequest(UserService userService, UserRepository userRepository, User user) {
        this.userService = userService;
        this.userRepository = userRepository;
        this.user = user;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Optional<User> getUserId(String userId){
        return userRepository.findUserByUserId(userId);
    }

    public Optional<User> getPassword(String password){
        return userRepository.findUserByPassword(password);
    }


    public UserService getUserService() {
        return userService;
    }


    public User getUser() {
        return user;
    }
}