package com.azry.lms.service.impl;

import com.azry.lms.dto.response.BookResponse;
import com.azry.lms.dto.response.UserResponse;
import com.azry.lms.entity.User;
import com.azry.lms.exception.ResourceNotFoundException;
import com.azry.lms.repository.UserRepository;
import com.azry.lms.service.UserService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;

    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
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
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", id));

        return user.getBorrowedBooks()
                .stream()
                .map(BookResponse::ofEntity)
                .collect(Collectors.toList());
    }
}
