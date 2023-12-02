package com.azry.lms.controller;

import com.azry.lms.dto.request.AuthRequest;
import com.azry.lms.dto.response.AuthResponse;
import com.azry.lms.dto.response.UserResponse;
import com.azry.lms.service.AuthService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping(value = {"/login", "/signin"})
    public AuthResponse login(@RequestBody @Valid AuthRequest request) {
        return authService.login(request);
    }

    @PostMapping(value = {"/register", "/signup"})
    @ResponseStatus(HttpStatus.CREATED)
    public UserResponse register(@RequestBody @Valid AuthRequest request) {
        return authService.register(request);
    }
}
