package com.azry.lms.controller;

import com.azry.lms.dto.response.BookResponse;
import com.azry.lms.dto.response.UserResponse;
import com.azry.lms.service.UserService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

// TODO: 12/2/2023 add security by admin role
@RestController
@RequestMapping("/api/users")
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public List<UserResponse> getAllUsers() {
        return userService.getAllUsers();
    }

    @GetMapping("/{id}/books")
    public List<BookResponse> getBorrowedBooksOfUser(@PathVariable Long id) {
        return userService.getBorrowedBooksOfUser(id);
    }

    // TODO: 12/2/2023 add user creation, edit, delete apis after adding security to app
}
