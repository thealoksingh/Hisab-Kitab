package com.hisabKitab.springProject.exception;

import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.hisabKitab.springProject.dto.CommonResponseDto;
import com.hisabKitab.springProject.utils.ResponseBuilder;

import io.jsonwebtoken.ExpiredJwtException;
import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.ServletException;

import org.slf4j.Logger;

@RestControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(EntityAlreadyExistException.class)
    public ResponseEntity<CommonResponseDto<Object>> entityAlreadyExistException(EntityAlreadyExistException exception) {
        logger.error(exception.getMessage());
        return ResponseBuilder.failure(HttpStatus.BAD_REQUEST, exception.getMessage());
    }

    @ExceptionHandler(UnAuthorizedException.class)
    public ResponseEntity<CommonResponseDto<Object>> unAuthorizedException(UnAuthorizedException exception) {
        logger.error(exception.getMessage());
        return ResponseBuilder.failure(HttpStatus.BAD_REQUEST, exception.getMessage());
    }

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<CommonResponseDto<Object>> entityNotFoundException(EntityNotFoundException exception) {
        logger.error(exception.getMessage());
        return ResponseBuilder.failure(HttpStatus.BAD_REQUEST, exception.getMessage());
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<CommonResponseDto<Object>> handleBadCredentials(BadCredentialsException exception) {
        logger.error(exception.getMessage());
        return ResponseBuilder.failure(HttpStatus.UNAUTHORIZED, exception.getMessage());
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<CommonResponseDto<Object>> handleAccessDenied(AccessDeniedException exception) {
        logger.error(exception.getMessage());
        return ResponseBuilder.failure(HttpStatus.FORBIDDEN, exception.getMessage());
    }

    @ExceptionHandler(InvalidTokenException.class)
    public ResponseEntity<CommonResponseDto<Object>> handleInvalidTokenException(InvalidTokenException exception) {
        logger.error(exception.getMessage());
        return ResponseBuilder.failure(HttpStatus.BAD_REQUEST, exception.getMessage());
    }

    @ExceptionHandler(TokenExpiredException.class)
    public ResponseEntity<CommonResponseDto<Object>> handleTokenExpiredException(TokenExpiredException exception) {
        logger.error(exception.getMessage());
        return ResponseBuilder.failure(HttpStatus.UNAUTHORIZED, exception.getMessage());
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<CommonResponseDto<Object>> handleAllExceptions(Exception exception) {
        logger.error("Unhandled exception: {}", exception.getMessage());
        return ResponseBuilder.failure(HttpStatus.INTERNAL_SERVER_ERROR, "Something went wrong");
    }

    @ExceptionHandler(TokenRefreshException.class)
    public ResponseEntity<CommonResponseDto<Object>> handleTokenRefreshExceptions(TokenRefreshException exception) {
        logger.error("Refresh token exception: {}", exception.getMessage());
        return ResponseBuilder.failure(HttpStatus.UNAUTHORIZED, exception.getMessage());
    }

    @ExceptionHandler(ExpiredJwtException.class)
    public ResponseEntity<CommonResponseDto<Object>> handleJwtExpireExceptions(ExpiredJwtException exception) {
        logger.error("Access token expired: {}", exception.getMessage());
        return ResponseBuilder.failure(HttpStatus.UNAUTHORIZED, exception.getMessage());
    }

    @ExceptionHandler(ServletException.class)
    public ResponseEntity<CommonResponseDto<Object>> handleServletExceptions(ServletException exception) {
        logger.error("Access token expired: {}", exception.getMessage());
        return ResponseBuilder.failure(HttpStatus.UNAUTHORIZED, exception.getMessage());
    }
}