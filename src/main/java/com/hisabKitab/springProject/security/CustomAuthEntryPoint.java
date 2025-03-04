package com.hisabKitab.springProject.security;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@Component
public class CustomAuthEntryPoint implements AuthenticationEntryPoint {

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException)
            throws IOException, ServletException {

        String message = "Authentication failed.";
        int status = HttpServletResponse.SC_UNAUTHORIZED;
                System.out.println("commence called");
        if (authException.getCause() instanceof io.jsonwebtoken.ExpiredJwtException) {
            message = "Token has expired. Please log in again.";
        } else if (authException.getCause() instanceof io.jsonwebtoken.MalformedJwtException) {
            message = "Invalid authentication token.";
            status = HttpServletResponse.SC_BAD_REQUEST;
        } else if (authException.getMessage().contains("Full authentication is required")) {
            message = "Authentication token required.";
        }

        response.setStatus(status);
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write("{\"error\": \"" + message + "\"}");
    }
}
