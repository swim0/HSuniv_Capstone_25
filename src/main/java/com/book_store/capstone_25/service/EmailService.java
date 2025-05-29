package com.book_store.capstone_25.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.util.Random;

@Service
public class EmailService {

    private final JavaMailSender mailSender;

    @Autowired
    public EmailService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    /* ───────────────────────── 인증 코드 메일 ───────────────────────── */

    public void sendAuthenticationCode(String email, String code) {
        sendMail(
                email,
                "비밀번호 재설정 인증 코드",
                "요청하신 인증 코드는 아래와 같습니다.\n\n" + code +
                        "\n\n※ 인증 코드는 10분간만 유효합니다."
        );
    }

    /* ───────────────────────── 비밀번호 안내 메일 ───────────────────────── */

    /** 인증 성공 후 비밀번호(또는 임시비밀번호)를 발송 */
    public void sendPassword(String email, String password) {
        sendMail(
                email,
                "요청하신 비밀번호 안내",
                "회원님의 비밀번호는 아래와 같습니다.\n\n" +
                        password
        );
    }

    /* ───────────────────────── 공통 메일 전송 로직 ───────────────────────── */

    private void sendMail(String to, String subject, String text) {
        SimpleMailMessage msg = new SimpleMailMessage();
        msg.setTo(to);
        msg.setSubject(subject);
        msg.setText(text);
        mailSender.send(msg);
    }

    /* ───────────────────────── 인증 코드 생성 ───────────────────────── */

    public static String generateAuthenticationCode() {
        Random rnd = new Random();
        StringBuilder code = new StringBuilder(6);
        for (int i = 0; i < 6; i++) {
            code.append(rnd.nextInt(10)); // 0~9
        }
        return code.toString();
    }
}
