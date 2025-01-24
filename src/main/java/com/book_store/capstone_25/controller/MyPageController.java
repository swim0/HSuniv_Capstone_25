package com.book_store.capstone_25.controller;

import com.book_store.capstone_25.Repository.InterestRepository;
import com.book_store.capstone_25.Repository.UserOrderRepository;
import com.book_store.capstone_25.Repository.UserRepository;
import com.book_store.capstone_25.model.User_order;
import com.book_store.capstone_25.model.User;
import com.book_store.capstone_25.model.UserInterest;
import lombok.Setter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;


@RestController
@RequestMapping({"/member"})
@Setter
public class MyPageController {
    private final UserOrderRepository userOrderRepository;
    private final UserRepository userRepository;
    private final InterestRepository interestRepository;

    public MyPageController(UserOrderRepository userOrderRepository, UserRepository userRepository, InterestRepository interestRepository){
        this.userOrderRepository = userOrderRepository;
        this.userRepository = userRepository;
        this.interestRepository = interestRepository;
    }

    @PutMapping("MyPage/{userId}")
    public ResponseEntity<User> updateUser(@PathVariable("userId") String userId,
                                           @RequestParam("password") String password,
                                           @RequestBody User updatedUser) {
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

        User savedUser = userRepository.save(user);
        return ResponseEntity.ok(savedUser);
    }
    @PostMapping("/MyPage/delete")
    public ResponseEntity<String> deleteUser(@RequestParam String password) {
        // 비밀번호로 사용자를 검증.
        Optional<User> user = userRepository.findUserByPassword(password);
        if (user.isEmpty()) {
            // 해당 아이디와 비밀번호를 가진 사용자가 없는 경우 에러 메시지와 함께 HTTP 400 상태 코드를 반환합니다.
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("아이디나 비밀번호가 잘못되었습니다.");
        }
        // 사용자를 제거합니다.
        userRepository.deleteByUserId(user.get().getUserId());
        // 회원 탈퇴 성공 메시지와 함께 HTTP 200 상태 코드를 반환합니다.
        return ResponseEntity.ok("회원 탈퇴가 성공적으로 완료되었습니다.");
    }

    @GetMapping("/MyPage/{userId}/orders")
    public ResponseEntity<List<User_order>> getOrders(@PathVariable("userId") String userId) {
        List<User_order> userorders = userOrderRepository.findByUserId(userId);
        return ResponseEntity.ok(userorders);
    }


    @GetMapping("/{userId}/interests")
    public ResponseEntity<List<UserInterest>> getUserInterests(@PathVariable("userId") String userId) {
        List<UserInterest> userInterests = interestRepository.findByUserId(userId);
        return ResponseEntity.ok(userInterests);
    }

    @PostMapping("/{userId}/interests")
    public ResponseEntity<UserInterest> createUserInterest(@PathVariable("userId") String userId, @RequestBody UserInterest userInterest) {
        userInterest.setUserId(userId);
        UserInterest newUserInterest = interestRepository.save(userInterest);
        return ResponseEntity.status(HttpStatus.CREATED).body(newUserInterest);
    }

    @DeleteMapping("/{userId}/interests/{interestId}")
    public ResponseEntity<Void> deleteUserInterest(@PathVariable("userId") String userId, @PathVariable("interestId") Long interestId) {
        interestRepository.deleteByUserIdAndInterestId(userId, interestId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}


