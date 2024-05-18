package com.projects.userservice.controllers;

import com.projects.userservice.dtos.LoginRequestDTO;
import com.projects.userservice.dtos.LogoutRequestDTO;
import com.projects.userservice.dtos.SignupRequestDTO;
import com.projects.userservice.dtos.ValidateTokenRequestDTO;
import com.projects.userservice.exceptions.ExpiredTokenException;
import com.projects.userservice.exceptions.InvalidTokenException;
import com.projects.userservice.models.Token;
import com.projects.userservice.models.User;
import com.projects.userservice.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
            // TODO - add basic validations
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
            // TODO - add basic validations
            Token token = this.userService.login(email, password);
            return new ResponseEntity<>(token, HttpStatus.OK);
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
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        } catch (ExpiredTokenException ete) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
    }

}
