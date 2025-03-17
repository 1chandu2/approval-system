package org.example.controller;


import jakarta.validation.Valid;
import org.example.config.JwtUtil;
import org.example.dto.LoginRequest;
import org.example.dto.LoginResponse;
import org.example.dto.UserResponse;
import org.example.dto.UserSignUpRequest;
import org.example.entity.User;
import org.example.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/user")
public class UserController {


    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private UserService userService;

    @RequestMapping(path = "/signUp", method = RequestMethod.POST)
    public ResponseEntity<LoginResponse> userSignUp(@Valid @RequestBody UserSignUpRequest signUpRequest) {
        signUpRequest.setPassword(passwordEncoder.encode(signUpRequest.getPassword()));
        userService.saveUser(signUpRequest);
        String token = jwtUtil.generateToken(signUpRequest.getUserName());
        return ResponseEntity.status(HttpStatus.CREATED).body(new LoginResponse(token));
    }

    @RequestMapping(path = "/login", method = RequestMethod.POST)
    public ResponseEntity<LoginResponse> login(@Valid @RequestBody LoginRequest loginRequest) {
        User dbUser = userService.getUserByUserName(loginRequest.getUserName());

        if (!passwordEncoder.matches(loginRequest.getPassword(), dbUser.getPassword())) {
            throw new RuntimeException("Invalid credentials");
        }

        String token = jwtUtil.generateToken(dbUser.getUserName());
        return ResponseEntity.status(HttpStatus.OK).body(new LoginResponse(token));
    }

    @RequestMapping(path = "/", method = RequestMethod.GET)
    public ResponseEntity<List<UserResponse>> getAllUser() {
        List<UserResponse> users = userService.getAllUser();
        return ResponseEntity.status(HttpStatus.OK).body(users);
    }
}
