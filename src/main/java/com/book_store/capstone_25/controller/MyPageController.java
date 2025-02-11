package com.book_store.capstone_25.controller;

import com.book_store.capstone_25.Repository.InterestRepository;
import com.book_store.capstone_25.Repository.UserOrderRepository;
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
    private final UserOrderRepository userOrderRepository;
    private final UserRepository userRepository;
    private final InterestRepository interestRepository;

    public MyPageController(UserOrderRepository userOrderRepository, UserRepository userRepository, InterestRepository interestRepository, UserService userService){
        this.userOrderRepository = userOrderRepository;
        this.userRepository = userRepository;
        this.interestRepository = interestRepository;

    }

    @PutMapping("MyPage/{userId}")
    public ResponseEntity<User> MyPage_vaild(@PathVariable("userId") String userId,
                                             @RequestParam("password") String password,
                                             @RequestBody User updatedUser,
                                             @RequestBody User updatePayment)
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
        if(updatePayment.getCardType() != null) user.setCardType(updatePayment.getCardType());
        if(updatePayment.getCardNumber() != null) user.setCardNumber(updatePayment.getCardNumber());

        User savedUser = userRepository.save(user);
        return ResponseEntity.ok(savedUser);
    }


    @Transactional
    @DeleteMapping("/MyPage/{userId}")
    public ResponseEntity<Map<String, String>> deleteUser(@PathVariable String userId,@RequestParam String password) {
        Optional<User> user = userRepository.findUserByUserIdAndPassword(userId,password);

        if (user.isEmpty()) {
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(Map.of("message", "아이디나 비밀번호가 잘못되었습니다"));
        }

        userRepository.deleteUserByUserId(userId);
        return ResponseEntity.ok(Map.of("message", "회원 탈퇴가 성공적으로 완료되었습니다."));
    }


        // 사용자 관심목록 관련 API
    @GetMapping("/MyPage/{userId}/interests")
    public ResponseEntity<List<User_Interest>> getUserInterests(@PathVariable("userId") User userId) {
        Optional<User> user = userRepository.findUserByUserId(userId.getUserId());
        // 2. 사용자가 존재하지 않으면 404 Not Found 응답 반환
        if (user == null) {
            return ResponseEntity.notFound().build();
        }
        // 3. 사용자의 관심사 목록 조회
        ResponseEntity<List<User_Interest>> userInterests = getUserInterests(userId);

        // 4. 관심사 목록 반환
        return ResponseEntity.ok(userInterests.getBody());
    }


    @PostMapping("/MyPage/{userId}/add_interests")
    public ResponseEntity<User_Interest> createUserInterest(@PathVariable("userId") String userId, @RequestParam User_Interest.Genre genre) {
            User user = userRepository.findUserByUserId(userId)
                    .orElseThrow(() -> new IllegalArgumentException("Invalid user Id:" + userId));

            User_Interest userInterest = new User_Interest();
            userInterest.setUser(user); // User 객체 설정
            userInterest.setGenre(genre);

            interestRepository.save(userInterest);
            return ResponseEntity.status(HttpStatus.CREATED).body(userInterest);
        }

    @DeleteMapping("/MyPage/{userId}/delete_interests")
    public ResponseEntity<Void> deleteUserInterest(@PathVariable("userId")@RequestBody User userId, @RequestBody User_Interest.Genre genre) {
        interestRepository.deleteUser_InterestByUserAndGenre(userId, genre);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}


