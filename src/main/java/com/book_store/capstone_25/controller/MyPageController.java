package com.book_store.capstone_25.controller;

import com.book_store.capstone_25.Repository.InterestRepository;
import com.book_store.capstone_25.Repository.UserOrderRepository;
import com.book_store.capstone_25.Repository.UserRepository;
import com.book_store.capstone_25.model.User;
import com.book_store.capstone_25.model.UserInterest;
import com.book_store.capstone_25.service.UserService;
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
    private final UserService userService;
    public MyPageController(UserOrderRepository userOrderRepository, UserRepository userRepository, InterestRepository interestRepository, UserService userService){
        this.userOrderRepository = userOrderRepository;
        this.userRepository = userRepository;
        this.interestRepository = interestRepository;
        this.userService = userService;
    }

    @PutMapping("/MyPage/{userId}")
    public ResponseEntity<User> MyPage_vaild(@PathVariable("userId") String userId,
                                           @RequestParam("password") String password,
                                           @RequestBody User updatedUser) {
        User user = userRepository.findUserByUserIdAndPassword(userId, password)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "사용자를 찾을 수 없습니다."));

        if (!user.toString().equals(password)) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "비밀번호가 틀렸습니다.");
        }

        if (updatedUser.getPhoneNumber() != null) updatedUser.setPhoneNumber(updatedUser.getPhoneNumber());
        if (updatedUser.getEmail() != null) updatedUser.setEmail(updatedUser.getEmail());
        if (updatedUser.getAddress() != null) updatedUser.setAddress(updatedUser.getAddress());
        if (updatedUser.getName() != null) updatedUser.setName(updatedUser.getName());
        if (updatedUser.getBirthDate() != null) updatedUser.setBirthDate(updatedUser.getBirthDate());
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
        userRepository.deleteByUserId(String.valueOf(user));
        // 회원 탈퇴 성공 메시지와 함께 HTTP 200 상태 코드를 반환합니다.
        return ResponseEntity.ok("회원 탈퇴가 성공적으로 완료되었습니다.");
    }

    @GetMapping("/MyPage/{userId}/orders")
    public ResponseEntity<Optional<User>> getOrders(@PathVariable("userId") String userId) {
        Optional<User> userorders = userRepository.findUserByUserId(userId);
        return ResponseEntity.ok(userorders);
    }


    @GetMapping("/{userId}/interests")
    public ResponseEntity<List<UserInterest>> getUserInterests(@PathVariable("userId") String userId) {
        Optional<User> user = userRepository.findUserByUserId(userId);
        // 2. 사용자가 존재하지 않으면 404 Not Found 응답 반환
        if (user == null) {
            return ResponseEntity.notFound().build();
        }
        // 3. 사용자의 관심사 목록 조회
        ResponseEntity<List<UserInterest>> userInterests = getUserInterests(userId);

        // 4. 관심사 목록 반환
        return ResponseEntity.ok(userInterests.getBody());
    }

    @PostMapping("/{userId}/interests")
    public ResponseEntity<UserInterest> createUserInterest(@PathVariable("userId")@RequestBody UserInterest userInterest) {
        UserInterest newUserInterest = interestRepository.save(userInterest);
        return ResponseEntity.status(HttpStatus.CREATED).body(newUserInterest);
    }



    /*@DeleteMapping("/{userId}/interests")
    public ResponseEntity<Void> deleteUserInterest(@PathVariable("userId/interests")@RequestBody String userId,@RequestBody Long interestId) {
        interestRepository.deleteByUserIdAndInterestId(userId,interestId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }*/
}


