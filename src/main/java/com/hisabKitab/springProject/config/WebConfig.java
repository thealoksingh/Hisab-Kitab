// package com.hisabKitab.springProject.config;

// import org.springframework.context.annotation.Configuration;
// import org.springframework.web.servlet.config.annotation.CorsRegistry;
// import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

// @Configuration
// public class WebConfig implements WebMvcConfigurer {
//     @Override
//     public void addCorsMappings(CorsRegistry registry) {
//         registry.addMapping("/**")
//                 .allowedOrigins("*") // Allow requests from any origin
//                 .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
//                 .allowedHeaders("*")
//                 .exposedHeaders("Authorization", "Content-Type", "error", "status")
//                 .allowCredentials(true); // Add "status" and "error" to the list of exposed headers
//     }
// }

