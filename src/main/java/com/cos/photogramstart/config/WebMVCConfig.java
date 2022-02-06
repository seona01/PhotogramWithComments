package com.cos.photogramstart.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.resource.PathResourceResolver;

@Configuration
public class WebMVCConfig implements WebMvcConfigurer {         // web설정파일

    @Value("${file.path}")
    private String uploadFolder;

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        WebMvcConfigurer.super.addResourceHandlers(registry);

        registry.addResourceHandler("/upload/**")           // .jsp에서 /upload/** 패턴 나오면 동작
                .addResourceLocations("file:///" + uploadFolder)
                .setCachePeriod(60*10*6)        // 이미지 캐싱 1시간 설정
                .resourceChain(true)    // true : 발동
                .addResolver(new PathResourceResolver());
    }
}
