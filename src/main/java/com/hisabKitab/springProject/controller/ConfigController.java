package com.hisabKitab.springProject.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/config")
public class ConfigController {

    @Autowired
    private Environment env;

    @GetMapping("/properties")
    public Map<String, String> getAllProperties() {
        return System.getenv();
    }

    @GetMapping("/app-properties")
    public Map<String, Object> getAppProperties() {
        return Map.of(
            "server.port", env.getProperty("server.port"),
            "spring.datasource.url", env.getProperty("spring.datasource.url"),
            "spring.kafka.bootstrap-servers", env.getProperty("spring.kafka.bootstrap-servers"),
            "jwt.secret", env.getProperty("jwt.secret") != null ? "***HIDDEN***" : null
        );
    }
}