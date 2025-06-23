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

import io.jsonwebtoken.ExpiredJwtException;
import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.ServletException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RestControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

	private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

	@ExceptionHandler(EntityAlreadyExistException.class)
	public ResponseEntity<CommonResponseDto<Object>> entityNotFoundException(EntityAlreadyExistException exception) {

		var errorMessage = new CommonResponseDto<>(HttpStatus.BAD_REQUEST, exception.getMessage(), null);

		logger.error(exception.getMessage());

		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorMessage);

	}

	@ExceptionHandler(UnAuthorizedException.class)
	public ResponseEntity<CommonResponseDto<Object>> unAuthorizedException(UnAuthorizedException exception) {

		var errorMessage = new CommonResponseDto<>(HttpStatus.BAD_REQUEST, exception.getMessage(), null);

		logger.error(exception.getMessage());

		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorMessage);

	}

	@ExceptionHandler(EntityNotFoundException.class)
	public ResponseEntity<CommonResponseDto<Object>> entityNotFoundException(EntityNotFoundException exception) {

		var errorMessage = new CommonResponseDto<>(HttpStatus.BAD_REQUEST, exception.getMessage(), null);

		logger.error(exception.getMessage());

		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorMessage);

	}

	@ExceptionHandler(BadCredentialsException.class)
	public ResponseEntity<String> handleBadCredentials(BadCredentialsException exception) {
		logger.error(exception.getMessage());
		return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(exception.getMessage());
	}

	@ExceptionHandler(AccessDeniedException.class)
	public ResponseEntity<String> handleAccessDenied(AccessDeniedException exception) {
		logger.error(exception.getMessage());
		return ResponseEntity.status(HttpStatus.FORBIDDEN).body(exception.getMessage());
	}

	@ExceptionHandler(InvalidTokenException.class)
	public ResponseEntity<CommonResponseDto<Object>> handleInvalidTokenException(InvalidTokenException exception) {

		var errorMessage = new CommonResponseDto<>(HttpStatus.BAD_REQUEST, exception.getMessage(), null);

		logger.error(exception.getMessage());

		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorMessage);

	}

	@ExceptionHandler(TokenExpiredException.class)
	public ResponseEntity<CommonResponseDto<Object>> handleTokenExpiredException(TokenExpiredException exception) {

		var errorMessage = new CommonResponseDto<>(HttpStatus.UNAUTHORIZED, exception.getMessage(), null);

		logger.error(exception.getMessage());

		return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errorMessage);

	}

	@ExceptionHandler(Exception.class)
	public ResponseEntity<CommonResponseDto<Object>> handleAllExceptions(Exception exception) {
		logger.error("Unhandled exception: {}", exception.getMessage());
		var errorMessage = new CommonResponseDto<>(HttpStatus.INTERNAL_SERVER_ERROR, "Something went wrong", null);
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorMessage);
	}
	@ExceptionHandler(TokenRefreshException.class)
	public ResponseEntity<CommonResponseDto<Object>> handleTokenRefreshExceptions(TokenRefreshException exception) {
		logger.error("Refresh token exception: {}", exception.getMessage());
		var errorMessage = new CommonResponseDto<>(HttpStatus.UNAUTHORIZED,  exception.getMessage(), null);
		return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errorMessage);
	}
	@ExceptionHandler(ExpiredJwtException.class)
	public ResponseEntity<CommonResponseDto<Object>> handleJwtExpireExceptions(ExpiredJwtException exception) {
		logger.error("Access token expired: {}", exception.getMessage());
		var errorMessage = new CommonResponseDto<>(HttpStatus.UNAUTHORIZED,  exception.getMessage(), null);
		return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errorMessage);
	}
	@ExceptionHandler(ServletException.class)
	public ResponseEntity<CommonResponseDto<Object>> handleServletExceptions(ServletException exception) {
		logger.error("Access token expired: {}", exception.getMessage());
		var errorMessage = new CommonResponseDto<>(HttpStatus.UNAUTHORIZED,  exception.getMessage(), null);
		return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errorMessage);
	}


}
