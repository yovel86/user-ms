package com.projects.userservice.services;

import com.projects.userservice.exceptions.UserAlreadyExistsException;
import com.projects.userservice.models.User;
import com.projects.userservice.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.userRepository = userRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

    @Override
    public User signUp(String name, String email, String password) throws UserAlreadyExistsException {
        Optional<User> userOptional = this.userRepository.findUserByEmail(email);
        if(userOptional.isPresent()) throw new UserAlreadyExistsException("User already exists");
        User user = new User();
        user.setName(name);
        user.setEmail(email);
        String encodedPassword = this.bCryptPasswordEncoder.encode(password);
        user.setPassword(encodedPassword);
        return this.userRepository.save(user);
    }

}
