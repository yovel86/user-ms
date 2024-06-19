package com.projects.userservice.services;

import com.projects.userservice.dtos.UserProjection;
import com.projects.userservice.exceptions.*;
import com.projects.userservice.models.Token;
import com.projects.userservice.models.User;
import com.projects.userservice.repositories.TokenRepository;
import com.projects.userservice.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Calendar;
import java.util.Date;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

    private final JwtService jwtService;
    private final UserRepository userRepository;
    private final TokenRepository tokenRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    public UserServiceImpl(JwtService jwtService, UserRepository userRepository, TokenRepository tokenRepository, BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.jwtService = jwtService;
        this.userRepository = userRepository;
        this.tokenRepository = tokenRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

    @Override
    public User signup(String name, String email, String password) throws UserAlreadyExistsException {
        Optional<User> userOptional = this.userRepository.findUserByEmail(email);
        if(userOptional.isPresent()) throw new UserAlreadyExistsException("User already exists");
        User user = new User();
        user.setName(name);
        user.setEmail(email);
        String encodedPassword = this.bCryptPasswordEncoder.encode(password);
        user.setPassword(encodedPassword);
        return this.userRepository.save(user);
    }

    @Override
    public Token login(String email, String password) throws InvalidEmailException, InvalidPasswordException, SessionCountExceededException {
        Optional<User> userOptional = this.userRepository.findUserByEmail(email);
        if(userOptional.isEmpty()) throw new InvalidEmailException("Invalid Email ID");
        User user = userOptional.get();
        boolean passwordMatches = this.bCryptPasswordEncoder.matches(password, user.getPassword());
        if(passwordMatches) {
            // Session count check
            int numberOfActiveSessions = this.tokenRepository.findNumberOfActiveSessions(user.getId());
            if(numberOfActiveSessions >= 2) throw new SessionCountExceededException("At Max, Only 2 Active Sessions Are Allowed");
            // Issue a Token
            Calendar calendar = Calendar.getInstance();
            calendar.add(Calendar.DATE, 30);
            Date thirtyDaysLater = calendar.getTime();
            String value = this.jwtService.generateToken(user, thirtyDaysLater);
            Token token = new Token();
            token.setValue(value);
            token.setUser(user);
            token.setExpiresAt(thirtyDaysLater);
            token.setActive(true);
            return this.tokenRepository.save(token);
        } else {
            throw new InvalidPasswordException("Invalid Password");
        }
    }

    @Override
    public Token validateToken(String tokenValue) throws InvalidTokenException, ExpiredTokenException {
        Optional<Token> tokenOptional = this.tokenRepository.findByValue(tokenValue);
        if(tokenOptional.isEmpty()) throw new InvalidTokenException("Invalid Token");
        Token token = tokenOptional.get();
        Date expiresAt = token.getExpiresAt();
        Date now = new Date();
        if(now.after(expiresAt) || !token.isActive()) {  // check expiry
            throw new ExpiredTokenException("Token has expired");
        }
        return token;
    }

    @Override // Soft Delete the Token
    public void logout(String tokenValue) throws InvalidTokenException {
        Optional<Token> tokenOptional = this.tokenRepository.findByValue(tokenValue);
        if(tokenOptional.isEmpty()) throw new InvalidTokenException("Invalid Token");
        Token token = tokenOptional.get();
        if(token.isActive()) {
            token.setActive(false);
            this.tokenRepository.save(token);
        }
    }

    @Override
    public Optional<User> getUserById(long userId) {
        return this.userRepository.findById(userId);
    }

    @Override
    public Optional<UserProjection> getUserProfileById(long userId) {
        return this.userRepository.findProjectedById(userId);
    }

}
