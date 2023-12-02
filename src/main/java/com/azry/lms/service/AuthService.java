package com.azry.lms.service;

import com.azry.lms.dto.request.AuthRequest;
import com.azry.lms.dto.response.AuthResponse;
import com.azry.lms.dto.response.UserResponse;

public interface AuthService {
    AuthResponse login(AuthRequest request);

    UserResponse register(AuthRequest request);
}
