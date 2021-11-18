package com.kti.restaurant.exception;

public class MissingEntityException extends RuntimeException {
    private String message;

    public MissingEntityException() {

    }

    public MissingEntityException(String message) {
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
