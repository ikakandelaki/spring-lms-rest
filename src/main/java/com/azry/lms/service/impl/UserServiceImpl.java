package com.azry.lms.service.impl;

import com.azry.lms.dto.request.UserRequest;
import com.azry.lms.dto.response.BookResponse;
import com.azry.lms.dto.response.UserResponse;
import com.azry.lms.entity.User;
import com.azry.lms.entity.model.RoleName;
import com.azry.lms.entity.model.Status;
import com.azry.lms.exception.LmsException;
import com.azry.lms.exception.ResourceNotFoundException;
import com.azry.lms.repository.RoleRepository;
import com.azry.lms.repository.UserRepository;
import com.azry.lms.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final RoleRepository roleRepository;

    public UserServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder,
                           RoleRepository roleRepository) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.roleRepository = roleRepository;
    }

    @Override
    public List<UserResponse> getAllUsers() {
        return userRepository.findAll()
                .stream()
                .map(UserResponse::ofEntity)
                .collect(Collectors.toList());
    }

    @Override
    public List<BookResponse> getBorrowedBooksOfUser(Long id) {
        User user = getUser(id);

        return user.getBorrowedBooks()
                .stream()
                .map(BookResponse::ofEntity)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public UserResponse createUser(UserRequest request) {
        if (userRepository.existsByUsername(request.getUsername())) {
            throw new LmsException(HttpStatus.BAD_REQUEST, "Username already exists!");
        }
        User user = new User(request.getUsername(), passwordEncoder.encode(request.getPassword()));
        if (request.isAdmin()) {
            user.setRoles(roleRepository.findAll());
        } else {
            roleRepository.findByName(RoleName.ROLE_USER.name())
                    .ifPresent(role -> user.setRoles(List.of(role)));
        }
        User savedUser = userRepository.save(user);

        return UserResponse.ofEntity(savedUser);
    }

    @Override
    @Transactional
    public void deleteUser(Long id) {
        User user = getUser(id);
        user.getBorrowedBooks()
                .forEach(u -> {
                    u.setUser(null);
                    u.setStatus(Status.AVAILABLE.getName());
                });

        userRepository.delete(user);
    }

    private User getUser(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", id));
    }
}
