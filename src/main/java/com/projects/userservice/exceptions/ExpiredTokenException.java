package com.projects.userservice.exceptions;

public class ExpiredTokenException extends Exception {
    public ExpiredTokenException(String message) {
        super(message);
    }
}
