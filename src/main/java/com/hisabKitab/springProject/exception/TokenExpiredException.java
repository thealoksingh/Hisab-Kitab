package com.hisabKitab.springProject.exception;

import jakarta.servlet.ServletException;

public class TokenExpiredException extends ServletException {
    
    private static final long serialVersionUID = 1L;

    public TokenExpiredException(String message) {
        super(message);
    }

}
