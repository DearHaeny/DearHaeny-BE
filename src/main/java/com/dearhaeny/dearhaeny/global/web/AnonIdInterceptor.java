package com.dearhaeny.dearhaeny.global.web;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import java.util.UUID;

@Component
public class AnonIdInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {

        // 요청 헤더에서 anonId를 찾기
        String anonId = request.getHeader("anonId");

        // 헤더에 anonId가 없다면 쿠키에서 찾기
        if (anonId == null || anonId.isBlank()) {
            Cookie[] cookies = request.getCookies();

            // 기존 쿠키에서 anonId 찾기
            if (cookies != null) {
                for (Cookie cookie : cookies) {
                    if ("anonId".equals(cookie.getName())) {
                        anonId = cookie.getValue();
                        break;
                    }
                }
            }
        }

        // 요청 헤더와 쿠키에서 anonId를 찾을 수 없다면 새로 생성 후 발급
        if (anonId == null || anonId.isBlank()) {
            anonId = UUID.randomUUID().toString();
            Cookie newCookie = new Cookie("anonId", anonId);
            newCookie.setHttpOnly(true);
            newCookie.setPath("/");                     // 모든 경로에서 유효
            newCookie.setMaxAge(60 * 60 * 24 * 7);      // 7일 유지
            response.addCookie(newCookie);

            response.setHeader("anonId", anonId);
        }

        // controller에서 사용할 수 있도록 request에 저장
        request.setAttribute("anonId", anonId);
        return true;
    }

}
