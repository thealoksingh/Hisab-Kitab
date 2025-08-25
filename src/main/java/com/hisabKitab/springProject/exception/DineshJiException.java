package com.hisabKitab.springProject.exception;

public class DineshJiException extends RuntimeException {

    public DineshJiException(String message) {
        super(message);
    }

    public DineshJiException(String message, Throwable cause) {
        super(message, cause);
    }
}