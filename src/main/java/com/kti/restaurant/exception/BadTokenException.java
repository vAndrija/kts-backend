package com.kti.restaurant.exception;

public class BadTokenException extends RuntimeException{
    private String message;

    public BadTokenException() {

    }

    public BadTokenException(String message) {
        this.message = message;
    }

    @Override
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

}
