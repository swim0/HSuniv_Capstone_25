package com.book_store.capstone_25.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.util.Random;

@Service
public class EmailService {

    private JavaMailSender javaMailSender;

    @Autowired
    public EmailService(JavaMailSender javaMailSender) {
        this.javaMailSender = javaMailSender;
    }

    public void sendAuthenticationCode(String email, String code) {
        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setTo(email);
        mailMessage.setSubject("비밀번호 재설정 인증 코드");
        mailMessage.setText("인증 코드: " + code);

        javaMailSender.send(mailMessage);
    }

    // 랜덤 인증번호 생성
    public static String generateAuthenticationCode() {

        Random rnd = new Random();
        StringBuilder authCode = new StringBuilder(6);

        for (int i = 0; i < 6; i++) {
            // 0 - 9 범위의 랜덤 숫자 생성
            String randNum = Integer.toString(rnd.nextInt(10));

            // 생성한 랜덤 숫자를 문자열에 추가
            authCode.append(randNum);
        }

        return authCode.toString();

    }
}