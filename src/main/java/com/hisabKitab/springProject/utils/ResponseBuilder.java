package com.hisabKitab.springProject.utils;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.hisabKitab.springProject.dto.CommonResponseDto;

public class ResponseBuilder {

    // Successful response
    public static <T> ResponseEntity<CommonResponseDto<T>> success(HttpStatus status,String message, T entity) {
        return ResponseEntity.status(status)
                .body(buildResponse(status, message, entity));
    }

    // Failure response
    public static <T> ResponseEntity<CommonResponseDto<T>> failure(HttpStatus status, String message) {
        return ResponseEntity.status(status)
                .body(buildResponse(status, message, null));
    }
    // Failure response for Validation
    public static <T> ResponseEntity<CommonResponseDto<T>> failure(HttpStatus status, String message, T entity) {
        return ResponseEntity.status(status)
                .body(buildResponse(status, message, entity));
    }

    // Core builder
    private static <T> CommonResponseDto<T> buildResponse(HttpStatus status, String message, T entity) {
        return new CommonResponseDto<>(
            status,
            message,
            entity
        );
    }
}