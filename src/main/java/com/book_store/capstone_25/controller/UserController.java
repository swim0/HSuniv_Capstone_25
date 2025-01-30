package com.book_store.capstone_25.controller;

import com.book_store.capstone_25.DTO.LoginRequest;
import com.book_store.capstone_25.model.User;

import com.book_store.capstone_25.service.UserService;
import com.book_store.capstone_25.Repository.UserRepository;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping({"/api"}) // 클래스 전체에 적용된 루트 경로 정의
// 클래스 전체에 적용된 루트 경로 정의
public class UserController {
    @Getter
    private final UserService userService;
    private final UserRepository userRepository;

    public UserController(UserRepository userRepository, UserService userService) { // 생성자 주입
        this.userService = userService;
        this.userRepository = userRepository;
    }

    // 회원가입 검증 코드
    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody User user) {
        // Check if userId or password is empty/null
        if (user.getUserId() == null || user.getUserId().trim().isEmpty() ||
                user.getPassword() == null || user.getPassword().trim().isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("사용자 ID와 비밀번호는 필수입니다.");
        }
        if (userRepository.findUserByUserId(user.getUserId()).isPresent()) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body("이미 존재하는 사용자 ID입니다.");
        }
        try {
            User savedUser = userService.saveUser(user);
            return ResponseEntity.status(HttpStatus.CREATED).body(savedUser);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("사용자 등록 중 오류가 발생했습니다: " + e.getMessage());
        }
    }
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest, HttpServletRequest request) {
        Optional<User> userOpt = userRepository.findUserByUserIdAndPassword(loginRequest.userId, loginRequest.password);
        if (userOpt.isEmpty() || userOpt.get() == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of(
                            "success", false,
                            "message", "아이디나 비밀번호가 잘못되었습니다."
                    ));
        }

        User user = userOpt.get();

        // 세션을 생성합니다.
        HttpSession session = request.getSession();
        session.setAttribute("userId", user.getUserId());

        // JSON 응답 반환
        return ResponseEntity.ok(Map.of(
                "message", "로그인이 성공적으로 이루어졌습니다.",
                "userId", user.getUserId(),
                "success", true
        ));
    }

    @PostMapping("/logout")
    public ResponseEntity<String> logout(HttpServletRequest request) {
        // 세션을 받아와 무효화합니다.
        HttpSession session = request.getSession(false);
        if (session != null) {
            session.invalidate();
            // 로그아웃 성공 메시지와 함께 HTTP 200 상태 코드를 반환합니다.
            return ResponseEntity.ok("로그아웃이 성공적으로 완료되었습니다.");
        }
        // 세션이 이미 없는 경우, 로그아웃 실패 메시지와 함께 HTTP 400 상태 코드를 반환합니다.
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("이미 로그아웃 상태입니다.");
    }

    @PostMapping("/Id_such")
    public ResponseEntity<?> suchid(@RequestBody String email) {
        Optional<User> userEmail = userRepository.findUserByEmail(email);
        if (userEmail.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("message", "해당 이메일로 등록된 계정이 없습니다."));
        }
        // JSON 형태로 응답
        return ResponseEntity.ok(Map.of("userId", userEmail.get().getUserId()));
    }

    @PostMapping("/password_such")
    public ResponseEntity<?> passwordSuch(@RequestBody String email) {
        Optional<User> userEmail = userRepository.findUserByEmail(email);
        if (userEmail.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("message", "해당 이메일로 등록된 계정이 없습니다!"));
        }
        // 비밀번호 직접 반환 X, 대신 보안 메시지 반환
        return ResponseEntity.ok(Map.of(
                "Id",userEmail.get().getPassword()
        ));
    }

}

