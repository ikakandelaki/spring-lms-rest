package com.azry.lms.service;

import com.azry.lms.dto.response.BookResponse;
import com.azry.lms.dto.response.UserResponse;

import java.util.List;

public interface UserService {
    List<UserResponse> getAllUsers();

    List<BookResponse> getBorrowedBooksOfUser(Long id);
}
