package com.book_store.capstone_25.controller;

import com.book_store.capstone_25.Repository.InterestRepository;
import com.book_store.capstone_25.Repository.UserRepository;
import com.book_store.capstone_25.model.User;
import com.book_store.capstone_25.model.User_Interest;
import com.book_store.capstone_25.service.UserService;
import jakarta.transaction.Transactional;
import lombok.Setter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Map;
import java.util.Optional;


@RestController
@RequestMapping({"/member"})
@Setter
public class MyPageController {

    private final UserRepository userRepository;
    private final InterestRepository interestRepository;
    private final UserService userService;

    public MyPageController( UserRepository userRepository, InterestRepository interestRepository, UserService userService){
        this.userRepository = userRepository;
        this.interestRepository = interestRepository;
        this.userService = userService;
    }

    @PutMapping("MyPage/{userId}")
    public ResponseEntity<User> MyPage_vaild(@PathVariable("userId") String userId,
                                             @RequestParam("password") String password,
                                             @RequestBody User updatedUser)
    {
        User user = userRepository.findUserByUserIdAndPassword(userId, password)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "사용자를 찾을 수 없습니다."));

        if (!user.getPassword().equals(password)) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "비밀번호가 틀렸습니다.");
        }

        if (updatedUser.getPhoneNumber() != null) user.setPhoneNumber(updatedUser.getPhoneNumber());
        if (updatedUser.getEmail() != null) user.setEmail(updatedUser.getEmail());
        if (updatedUser.getAddress() != null) user.setAddress(updatedUser.getAddress());
        if (updatedUser.getName() != null) user.setName(updatedUser.getName());
        if (updatedUser.getBirthDate() != null) user.setBirthDate(updatedUser.getBirthDate());
        if (updatedUser.getAge() != null) user.setAge(updatedUser.getAge());

        // 결제 정보 추가
        if(updatedUser.getCardType() != null) user.setCardType(updatedUser.getCardType());
        if(updatedUser.getCardNumber() != null) user.setCardNumber(updatedUser.getCardNumber());

        User savedUser = userRepository.save(user);
        return ResponseEntity.ok(savedUser);
    }


    @Transactional
    @DeleteMapping("/MyPage/{userId}")
    public ResponseEntity<Map<String, String>> deleteUser(@PathVariable String userId, @RequestParam String password) {
        Optional<User> user = userRepository.findUserByUserIdAndPassword(userId, password);
        if (user.isEmpty()) {
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(Map.of("message", "아이디나 비밀번호가 잘못되었습니다"));
        }

        User currentUser = user.get();

        // User와 연관된 UserOrder와 UserInterest를 삭제

        interestRepository.deleteAll(currentUser.getUserInterests());

        // 이제 안전하게 User를 삭제할 수 있습니다.
        userRepository.delete(currentUser);

        return ResponseEntity.ok(Map.of("message", "회원 탈퇴가 성공적으로 완료되었습니다."));
    }


    @GetMapping("/MyPage/{userId}/interests")
    public ResponseEntity<List<User_Interest>> getUserInterests(@PathVariable("userId") Long userId, @RequestParam User_Interest.Genre genre) {
        List<User_Interest> userInterests = interestRepository.findByUser_IdAndGenresContains(userId, genre);
        if (userInterests.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(userInterests);
    }



    @PostMapping("/MyPage/{userId}/add_interests")
    public ResponseEntity<User_Interest> createUserInterest(
            @PathVariable("userId") String userId,
            @RequestParam User_Interest.Genre genre
    ) {
        User user = userRepository.findUserByUserId(userId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid user Id:" + userId));

        User_Interest userInterest = interestRepository.findByUser(user)
                .stream()
                .findFirst()
                .orElse(new User_Interest());

        userInterest.setUser(user);

        // 중복 방지 후 추가
        if (!userInterest.getGenres().contains(genre)) {
            userInterest.getGenres().add(genre);
            interestRepository.save(userInterest);
        }

        return ResponseEntity.status(HttpStatus.CREATED).body(userInterest);
    }

    @DeleteMapping("/MyPage/{userId}/delete_interests")
    public ResponseEntity<Void> deleteUserInterest(
            @PathVariable("userId") String userId,
            @RequestParam User_Interest.Genre genre
    ) {
        User user = userRepository.findUserByUserId(userId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid user Id:" + userId));

        User_Interest userInterest = interestRepository.findByUser(user)
                .stream()
                .findFirst()
                .orElse(null);

        if (userInterest != null && userInterest.getGenres().contains(genre)) {
            userInterest.getGenres().remove(genre);
            interestRepository.save(userInterest);
        }

        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}


