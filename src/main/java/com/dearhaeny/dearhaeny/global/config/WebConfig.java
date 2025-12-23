package com.dearhaeny.dearhaeny.global.config;

import com.dearhaeny.dearhaeny.global.web.AnonIdInterceptor;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@RequiredArgsConstructor
public class WebConfig implements WebMvcConfigurer {

    private final AnonIdInterceptor anonIdInterceptor;

    @Value("${FRONT}")
    private String frontOrigin;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(anonIdInterceptor)
                .addPathPatterns("/posts/**");              // /posts로 시작하는 모든 요청에 적용
    }

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins(frontOrigin) // 프론트 배포 주소
                .allowedMethods("GET", "POST", "PUT", "DELETE", "PATCH", "OPTIONS")
                .allowedHeaders("*") // 모든 헤더 허용 (Anonid 포함)
                .allowCredentials(true) // 쿠키/인증 정보 포함 허용
                .maxAge(3600);
    }
}
