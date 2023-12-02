package com.azry.lms.service.impl;

import com.azry.lms.dto.request.AuthRequest;
import com.azry.lms.dto.response.AuthResponse;
import com.azry.lms.dto.response.UserResponse;
import com.azry.lms.entity.User;
import com.azry.lms.entity.model.RoleName;
import com.azry.lms.exception.LmsException;
import com.azry.lms.repository.RoleRepository;
import com.azry.lms.repository.UserRepository;
import com.azry.lms.security.JwtTokenProvider;
import com.azry.lms.service.AuthService;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class AuthServiceImpl implements AuthService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider jwtTokenProvider;
    private final PasswordEncoder passwordEncoder;

    public AuthServiceImpl(
            UserRepository userRepository,
            RoleRepository roleRepository,
            AuthenticationManager authenticationManager,
            JwtTokenProvider jwtTokenProvider,
            PasswordEncoder passwordEncoder
    ) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.authenticationManager = authenticationManager;
        this.jwtTokenProvider = jwtTokenProvider;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public AuthResponse login(AuthRequest request) {
        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken =
                new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword());
        Authentication authentication = authenticationManager.authenticate(usernamePasswordAuthenticationToken);

        SecurityContextHolder.getContext().setAuthentication(authentication);

        String token = jwtTokenProvider.generateToken(authentication);
        return new AuthResponse(token);
    }

    @Override
    @Transactional
    public UserResponse register(AuthRequest request) {
        if (userRepository.existsByUsername(request.getUsername())) {
            throw new LmsException(HttpStatus.BAD_REQUEST, "Username already exists!");
        }

        User user = new User(request.getUsername(), passwordEncoder.encode(request.getPassword()));

        roleRepository.findByName(RoleName.ROLE_USER.name())
                .ifPresent(role -> user.setRoles(List.of(role)));
        User savedUser = userRepository.save(user);

        return UserResponse.ofEntity(savedUser);
    }
}
