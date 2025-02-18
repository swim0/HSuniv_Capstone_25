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

import java.util.*;


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
        userRepository.delete(currentUser);

        return ResponseEntity.ok(Map.of("message", "회원 탈퇴가 성공적으로 완료되었습니다."));
    }



    // 여기서부터 userId는 Long이므로 users 테이블의 id(유저번호) 입니다.
    // 해당 API는 특정 유저 관심분야 조회입니다.
    @GetMapping("/MyPage/{userId}/interests")
    public ResponseEntity<List<User_Interest.Genre>> getUserInterests(@PathVariable("userId") Long userId) {
        // 1. User_Interest 객체를 User ID로 조회
        Optional<User_Interest> userInterests = interestRepository.findByUser_Id(userId);

        // 2. 데이터가 없는 경우 404 응답
        if (userInterests.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        // 3. User_Interest 객체에서 genres 리스트 추출
        List<User_Interest.Genre> genres = userInterests.get().getGenres();

        // 4. genres 리스트가 비어 있으면 404 응답
        if (genres.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        // 5. genres 리스트 반환
        return ResponseEntity.ok(genres);
    }



    // 관심분야 추가
    @PostMapping("/MyPage/{userId}/add_interests")
    public ResponseEntity<User_Interest> createUserInterest(
            @PathVariable("userId") Long userId,
            @RequestParam String genre
    ) {
        // 1. User 객체 조회
        User user = (User) userRepository.findUsersById(userId)
                .orElseThrow(() -> new IllegalArgumentException("유효하지 않은 사용자 ID입니다."));

        // 2. User_Interest 가져오기 (없으면 새로 생성)
        User_Interest userInterest = interestRepository.findByUser_Id(userId)
                .orElse(new User_Interest());
        userInterest.setUser(user);  // User 설정

        // 3. genres 초기화 및 중복 확인
        List<User_Interest.Genre> genres = userInterest.getGenres();
        if (genres == null) {
            genres = new ArrayList<>();
        }

        // 4. 한글 문자열을 Enum으로 매핑
        User_Interest.Genre selectedGenre;
        try {
            selectedGenre = User_Interest.Genre.valueOf(genre);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(null); // 잘못된 Enum 값일 경우
        }

        // 5. 중복 확인 후 추가
        if (!genres.contains(selectedGenre)) {
            genres.add(selectedGenre);
            userInterest.setGenres(genres);
            interestRepository.save(userInterest);
        }

        return ResponseEntity.status(HttpStatus.CREATED).body(userInterest);
    }


    // 관심분야 삭제
    @DeleteMapping("/MyPage/{userId}/delete_interests")
    public ResponseEntity<Void> deleteUserInterest(
            @PathVariable("userId") Long userId,
            @RequestParam User_Interest.Genre genre
    ) {
        User user = (User) userRepository.findUsersById(userId)
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


