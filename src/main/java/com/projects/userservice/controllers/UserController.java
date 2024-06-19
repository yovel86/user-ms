package com.projects.userservice.controllers;

import com.projects.userservice.dtos.*;
import com.projects.userservice.exceptions.ExpiredTokenException;
import com.projects.userservice.exceptions.InvalidRequestException;
import com.projects.userservice.exceptions.InvalidTokenException;
import com.projects.userservice.models.Token;
import com.projects.userservice.models.User;
import com.projects.userservice.services.UserService;
import org.apache.coyote.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/signup")
    public ResponseEntity<User> signUp(@RequestBody SignupRequestDTO requestDTO) {
        String name = requestDTO.getName();
        String email = requestDTO.getEmail();
        String password = requestDTO.getPassword();
        try {
            if(name == null || name.isEmpty()) throw new InvalidRequestException("Name should not be empty");
            if(email == null || email.isEmpty()) throw new InvalidRequestException("Email should not be empty");
            if(password == null || password.isEmpty()) throw new InvalidRequestException("Password should not be empty");
            User user = this.userService.signup(name, email, password);
            return new ResponseEntity<>(user, HttpStatus.CREATED);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/login")
    public ResponseEntity<Token> login(@RequestBody LoginRequestDTO requestDTO) {
        String email = requestDTO.getEmail();
        String password = requestDTO.getPassword();
        try {
            if(email == null || email.isEmpty()) throw new InvalidRequestException("Email should not be empty");
            if(password == null || password.isEmpty()) throw new InvalidRequestException("Password should not be empty");
            Token token = this.userService.login(email, password);
            return new ResponseEntity<>(token, HttpStatus.OK);
        } catch (InvalidRequestException ire) {
            System.out.println(ire.getMessage());
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logout(@RequestBody LogoutRequestDTO requestDTO) {
        String tokenValue = requestDTO.getTokenValue();
        try {
            this.userService.logout(tokenValue);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/validate-token") // This method is not related to CRUD, so going with POST
    public ResponseEntity<Token> validateToken(@RequestBody ValidateTokenRequestDTO requestDTO) {
        String tokenValue = requestDTO.getTokenValue();
        try {
            Token token = this.userService.validateToken(tokenValue);
            return new ResponseEntity<>(token, HttpStatus.OK);
        } catch (InvalidTokenException ie) {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        } catch (ExpiredTokenException ete) {
            return new ResponseEntity<>(null, HttpStatus.UNAUTHORIZED);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<User> getUserById(@PathVariable("id") long userId) {
        Optional<User> userOptional = this.userService.getUserById(userId);
        if(userOptional.isEmpty()) return new ResponseEntity<>(null, HttpStatus.NO_CONTENT);
        return new ResponseEntity<>(userOptional.get(), HttpStatus.OK);
    }

    @GetMapping("/profile/{id}")
    public ResponseEntity<UserProjection> getUserProfileById(@PathVariable("id") long userId) {
        Optional<UserProjection> userOptional = this.userService.getUserProfileById(userId);
        if(userOptional.isEmpty()) return new ResponseEntity<>(null, HttpStatus.NO_CONTENT);
        return new ResponseEntity<>(userOptional.get(), HttpStatus.OK);
    }

    @GetMapping("/health")
    public ResponseEntity<String> healthCheck() {
        return ResponseEntity.ok("Server is running properly...");
    }

}
