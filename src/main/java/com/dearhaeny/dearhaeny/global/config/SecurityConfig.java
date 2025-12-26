package com.dearhaeny.dearhaeny.global.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                // 1. CSRF 비활성화 (Stateless API 서버이므로)
                .csrf(csrf -> csrf.disable())

                // 2. HTTP Basic 인증 및 Form Login 비활성화 (401의 원인 제거)
                .httpBasic(basic -> basic.disable())
                .formLogin(form -> form.disable())

                // 3. 경로별 권한 설정
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/posts/**").permitAll() // 게시글 관련 모든 요청 허용
                        .anyRequest().authenticated()             // 그 외 요청은 인증 필요
                );

        return http.build();
    }
}
