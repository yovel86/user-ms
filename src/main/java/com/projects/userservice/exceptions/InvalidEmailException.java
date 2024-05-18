package com.projects.userservice.exceptions;

public class InvalidEmailException extends Exception {
    public InvalidEmailException(String message) {
        super(message);
    }
}
