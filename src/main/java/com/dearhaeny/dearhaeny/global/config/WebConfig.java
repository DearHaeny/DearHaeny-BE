package com.dearhaeny.dearhaeny.global.config;

import com.dearhaeny.dearhaeny.global.web.AnonIdInterceptor;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@RequiredArgsConstructor
public class WebConfig implements WebMvcConfigurer {

    private final AnonIdInterceptor anonIdInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(anonIdInterceptor)
                .addPathPatterns("/posts/**");              // /posts로 시작하는 모든 요청에 적용
    }
}
