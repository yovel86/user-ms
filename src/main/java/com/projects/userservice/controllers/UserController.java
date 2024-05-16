package com.projects.userservice.controllers;

import com.projects.userservice.dtos.LoginRequestDTO;
import com.projects.userservice.dtos.SignupRequestDTO;
import com.projects.userservice.dtos.ValidateTokenRequestDTO;
import com.projects.userservice.models.Token;
import com.projects.userservice.models.User;
import com.projects.userservice.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
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
            User user = this.userService.signUp(name, email, password);
            return new ResponseEntity<>(user, HttpStatusCode.valueOf(201));
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return new ResponseEntity<>(HttpStatusCode.valueOf(400));
        }
    }

    @PostMapping("/login")
    public ResponseEntity<Token> login(@RequestBody LoginRequestDTO requestDTO) {
        return null;
    }

    @PostMapping("/validate-token") // This method is not related to CRUD, so going with POST
    public ResponseEntity<Token> validateToken(@RequestBody ValidateTokenRequestDTO requestDTO) {
        return null;
    }

}
