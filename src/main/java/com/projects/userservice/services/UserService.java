package com.projects.userservice.services;

import com.projects.userservice.exceptions.UserAlreadyExistsException;
import com.projects.userservice.models.User;
import org.springframework.stereotype.Service;

public interface UserService {

    User signUp(String name, String email, String password) throws UserAlreadyExistsException;

}
