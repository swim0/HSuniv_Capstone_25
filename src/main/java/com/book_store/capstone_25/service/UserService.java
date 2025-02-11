
package com.book_store.capstone_25.service;
import com.book_store.capstone_25.Repository.InterestRepository;
import com.book_store.capstone_25.model.User;
import java.util.Optional;
import com.book_store.capstone_25.Repository.UserRepository;

import com.book_store.capstone_25.model.User_Interest;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {
    public final UserRepository userRepository;
    private final InterestRepository interestRepository;
    private final PasswordEncoder passwordEncoder; // BCrypt를 사용

    public UserService(UserRepository userRepository, InterestRepository interestRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.interestRepository = interestRepository;
        this.passwordEncoder = passwordEncoder;
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

    public void addInterest(String userId, User_Interest.Genre genre) {
        User_Interest userInterest = new User_Interest();
        userInterest.getUser().setUserId(userId);
        userInterest.setGenre(genre);
        interestRepository.save(userInterest);
    }

    public User_Interest removeInterest(User user_id, User_Interest.Genre genre) {
        User_Interest userInterest = new User_Interest();
        userInterest.getUser().setUserId(user_id.getUserId());
        userInterest.setGenre(genre);
        return interestRepository.save(userInterest);
    }

    @Transactional
    public boolean deleteUser(String userId, String password) {
        Optional<User> user = userRepository.findByUserId(userId);

        if (user.isEmpty() || !passwordEncoder.matches(password, user.get().getPassword())) {
            return false; // 아이디나 비밀번호가 틀린 경우
        }

        userRepository.delete(user.get()); // 회원 삭제
        return true;
    }

}

