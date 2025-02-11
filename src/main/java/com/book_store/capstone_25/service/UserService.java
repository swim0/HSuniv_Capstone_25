
package com.book_store.capstone_25.service;
import com.book_store.capstone_25.Repository.InterestRepository;
import com.book_store.capstone_25.model.User;
import java.util.Optional;
import com.book_store.capstone_25.Repository.UserRepository;

import com.book_store.capstone_25.model.User_Interest;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {
    public final UserRepository userRepository;
    private final InterestRepository interestRepository;

    public UserService(UserRepository userRepository, InterestRepository interestRepository) {
        this.userRepository = userRepository;
        this.interestRepository = interestRepository;

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


}

