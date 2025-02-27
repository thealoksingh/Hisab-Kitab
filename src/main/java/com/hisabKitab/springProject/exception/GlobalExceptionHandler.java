package com.hisabKitab.springProject.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.hisabKitab.springProject.dto.CommonResponseDto;

import jakarta.persistence.EntityNotFoundException;


@RestControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {
 


	@ExceptionHandler(EntityAlreadyExistException.class)
	public ResponseEntity<CommonResponseDto<Object>> entityNotFoundException(EntityAlreadyExistException exception) {

		var errorMessage = new CommonResponseDto<>(HttpStatus.BAD_REQUEST, exception.getMessage(),null);

		logger.error(exception.getMessage());
 
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorMessage);

	}
	
	@ExceptionHandler(UnAuthorizedException.class)
	public ResponseEntity<CommonResponseDto<Object>> unAuthorizedException(UnAuthorizedException exception) {
		
		var errorMessage = new CommonResponseDto<>(HttpStatus.BAD_REQUEST, exception.getMessage(),null);
		
		logger.error(exception.getMessage());
		
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorMessage);
		
	}
	@ExceptionHandler(EntityNotFoundException.class)
	public ResponseEntity<CommonResponseDto<Object>> entityNotFoundException(EntityNotFoundException exception) {
		
		var errorMessage = new CommonResponseDto<>(HttpStatus.BAD_REQUEST, exception.getMessage(),null);
		
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
}
