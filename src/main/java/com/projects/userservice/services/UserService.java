package com.projects.userservice.services;

import com.projects.userservice.exceptions.*;
import com.projects.userservice.models.Token;
import com.projects.userservice.models.User;
import org.springframework.stereotype.Service;

public interface UserService {

    User signup(String name, String email, String password) throws UserAlreadyExistsException;

    Token login(String email, String password) throws InvalidEmailException, InvalidPasswordException;

    Token validateToken(String tokenValue) throws InvalidTokenException, ExpiredTokenException;

    void logout(String tokenValue) throws InvalidTokenException;

}
