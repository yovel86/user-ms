package com.projects.userservice.exceptions;

public class SessionCountExceededException extends Exception {
    public SessionCountExceededException(String message) {
        super(message);
    }
}
