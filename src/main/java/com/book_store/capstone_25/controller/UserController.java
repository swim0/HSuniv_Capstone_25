package com.book_store.capstone_25.controller;

import com.book_store.capstone_25.DTO.LoginRequest;
import com.book_store.capstone_25.Repository.UserSuchRepository;
import com.book_store.capstone_25.model.User;

import com.book_store.capstone_25.model.User_Such;
import com.book_store.capstone_25.service.EmailService;
import com.book_store.capstone_25.service.UserService;
import com.book_store.capstone_25.Repository.UserRepository;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;


import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequestMapping({"/api"}) // 클래스 전체에 적용된 루트 경로 정의
// 클래스 전체에 적용된 루트 경로 정의
public class UserController {
    @Getter
    private final UserService userService;
    private final EmailService emailServcie;
    private final UserRepository userRepository;;
    private final UserSuchRepository UserSuchRepository;


    public UserController(UserRepository userRepository, UserService userService, EmailService emailServcie, com.book_store.capstone_25.Repository.UserSuchRepository userSuchRepository, com.book_store.capstone_25.Repository.UserSuchRepository userSuchRepository1) { // 생성자 주입
        this.userService = userService;
        this.userRepository = userRepository;
        this.emailServcie = emailServcie;
        UserSuchRepository = userSuchRepository;
    }

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(
            @Valid @RequestBody User user,
            BindingResult br) {                         // ⬅️ 검증 결과

        /* 1) Bean-Validation 오류 → 400 */
        if (br.hasErrors()) {
            String msg = br.getFieldErrors().stream()
                    .map(f -> f.getField() + " : " + f.getDefaultMessage())
                    .collect(Collectors.joining(", "));
            return ResponseEntity.badRequest().body(msg);  // text/plain 또는 JSON으로
        }

        /* 2) ID 중복 검사 */
        if (userRepository.findUserByUserId(user.getUserId()).isPresent()) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body("이미 존재하는 로그인 ID입니다.");
        }

        /* 3) 저장 */
        User saved = userService.saveUser(user);
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }

    @GetMapping("/check-id")
    public ResponseEntity<?> checkDuplicateUserId(@RequestParam String userId) {

        if (userId == null || userId.trim().isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("message", "userId 파라미터가 필요합니다."));
        }

        boolean exists = userRepository.findUserByUserId(userId).isPresent();

        if (exists) {
            // 이미 존재 → 409
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(Map.of("message", "이미 존재하는 사용자 ID입니다.", "available", false));
        }

        // 사용 가능 → 200
        return ResponseEntity.ok(Map.of("message", "사용 가능한 ID입니다.", "available", true));
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest, HttpServletRequest request) {
        Optional<User> userOpt = userRepository.findUserByUserIdAndPassword(loginRequest.userId, loginRequest.password);
        if (userOpt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("아이디나 비밀번호가 잘못되었습니다.");
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
    public ResponseEntity<?> such_id(@RequestParam String email) {
        Optional<User> userEmail = userRepository.findUsersByEmail(email);
        if (userEmail.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("message", "해당 이메일로 등록된 계정이 없습니다."));
        }
        // JSON 형태로 응답
        return ResponseEntity.ok(Map.of("userId", userEmail.get().getUserId()));
    }

    @PostMapping("/password_such")
    public ResponseEntity<?> password_such(@RequestParam String email) {
        Optional<User> userOptional = userRepository.findUsersByEmail(email);

        if (userOptional.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("message", "해당 이메일로 등록된 계정이 없습니다!"));
        }

        User user = userOptional.get();

        // 기존 인증 정보가 있다면 삭제
        Optional<User_Such> existing = UserSuchRepository.findByUser(user);
        existing.ifPresent(UserSuchRepository::delete);

        // 인증 코드 생성
        String authenticationCode = EmailService.generateAuthenticationCode();
        String token = UUID.randomUUID().toString();

        // 새 인증 정보 저장
        UserSuchRepository.save(new User_Such(token, user, authenticationCode));

        // 이메일 전송
        emailServcie.sendAuthenticationCode(email, authenticationCode);

        return ResponseEntity.ok(Map.of("message", "인증 코드를 이메일로 보냈습니다."));
    }

    @PostMapping("/verify-code")
    public ResponseEntity<?> verifyCode(@RequestParam String email,
                                        @RequestParam String authenticationCode) {

        User_Such userSuch = UserSuchRepository
                .findUser_SuchByUserEmailAndAuthenticationCode(email, authenticationCode);

        if (userSuch == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("message", "잘못된 인증 코드입니다."));
        }

        // ① 비밀번호 가져오기
        String password = userSuch.getUser().getPassword();

        // ② 메일로 비밀번호 전송
        emailServcie.sendPassword(email, password);

        // ③ 인증 기록 삭제
        UserSuchRepository.delete(userSuch);

        // ④ 클라이언트에는 결과 메시지만
        return ResponseEntity.ok(Map.of(
                "message", "인증이 완료되었습니다. 비밀번호를 이메일로 전송했습니다."
        ));
    }

    @PostMapping("/infofind") //회원정보조회
    public ResponseEntity<?> userInfo(@RequestParam String userId) {
        Optional<User> userinfo = userRepository.findUserByUserId(userId);
        if (userinfo.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("message", "해당 아이디로 등록된 계정이 없습니다!"));
        }
        // User 객체 자체를 반환
        return ResponseEntity.ok(userinfo.get());
    }


}

