//package com.da.itdaing.global.config;
//
//import com.da.itdaing.global.file.FileStorageProperties;
//import lombok.RequiredArgsConstructor;
//import org.springframework.boot.context.properties.EnableConfigurationProperties;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
//import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
//
//import java.nio.file.Path;
//
//@Configuration
//@EnableConfigurationProperties(FileStorageProperties.class)
//@RequiredArgsConstructor
//public class WebConfig implements WebMvcConfigurer {
//
//    private final FileStorageProperties props;
//
//    @Override
//    public void addResourceHandlers(ResourceHandlerRegistry registry) {
//        // GET /uploads/** 를 로컬 폴더로 매핑
//        String location = Path.of(props.getBaseDir()).toUri().toString(); // "file:/.../"
//        registry.addResourceHandler(props.getBaseUrl() + "/**")
//            .addResourceLocations(location);
//    }
//}
