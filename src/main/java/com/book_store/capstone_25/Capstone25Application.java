package com.book_store.capstone_25;

import io.micrometer.common.lang.NonNull;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
@SpringBootApplication
public class Capstone25Application {

    public static void main(String[] args) {
        SpringApplication.run(Capstone25Application.class, args);
    }
    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(@NonNull CorsRegistry registry) {
                registry.addMapping("/**") // 모든 엔드포인트 허용
                        .allowedOriginPatterns("*") // 🌟 모든 도메인에서 접근 허용
                        .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS") // 모든 HTTP 메서드 허용
                        .allowedHeaders("*") // 모든 헤더 허용
                        .allowCredentials(true); // 인증 관련 요청도 허용 (쿠키 포함)
            }
        };
    }
}
