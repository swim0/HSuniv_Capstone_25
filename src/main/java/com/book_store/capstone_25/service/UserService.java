
package com.book_store.capstone_25.service;
import com.book_store.capstone_25.Repository.InterestRepository;
import com.book_store.capstone_25.model.User;
import java.util.Optional;
import com.book_store.capstone_25.Repository.UserRepository;

import com.book_store.capstone_25.model.UserInterest;
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

    public UserInterest addInterest(String userId, UserInterest.Genre genre) {
        UserInterest userInterest = new UserInterest();
        userInterest.setUserId(userId);
        userInterest.setGenre(genre);
        return interestRepository.save(userInterest);
    }

    public void deleteInterest(Long interestId) {
        interestRepository.deleteById(interestId);
    }
}

