package com.book_store.capstone_25;

import org.springframework.context.annotation.Configuration;
import org.springframework.lang.NonNull;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class CorsConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(@NonNull CorsRegistry registry) {
        registry.addMapping("/**") // 모든 엔드포인트 허용
                .allowedOriginPatterns("*") // 🌟 모든 도메인에서 접근 허용
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS") // 모든 HTTP 메서드 허용
                .allowedHeaders("*") // 모든 헤더 허용
                .allowCredentials(true); // 인증 관련 요청도 허용 (쿠키 포함)
    }
}
