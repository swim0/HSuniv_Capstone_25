
package com.book_store.capstone_25.service;
import com.book_store.capstone_25.Repository.InterestRepository;
import com.book_store.capstone_25.Repository.UserSuchRepository;
import com.book_store.capstone_25.model.User;
import java.util.Optional;
import com.book_store.capstone_25.Repository.UserRepository;

import com.book_store.capstone_25.model.User_Interest;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {
    public final UserRepository userRepository;
    private final InterestRepository interestRepository;
    private final UserSuchRepository userSuchRepository;

    public UserService(UserRepository userRepository, InterestRepository interestRepository, UserSuchRepository userSuchRepository) {
        this.userRepository = userRepository;
        this.interestRepository = interestRepository;

        this.userSuchRepository = userSuchRepository;
    }




    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public User saveUser(User userData) {
        return userRepository.save(userData);
    }
    public void registerUser(User user) {
        userRepository.save(user);
    }
    public User getUserIfValid(String userId, String password) throws Exception {
        Optional<User> optionalUser = userRepository.findUserByUserIdAndPassword(userId, password);
        if (optionalUser.isEmpty()) {
            throw new Exception("Invalid credentials");
        }
        return optionalUser.get();
    }


    @Transactional
    public void deleteUser(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("사용자를 찾을 수 없습니다: " + userId));

        // 1️⃣ `UserSuch` 먼저 삭제
        userSuchRepository.deleteByUser(user);

        // 2️⃣ `User` 삭제
        userRepository.delete(user);
    }



}

