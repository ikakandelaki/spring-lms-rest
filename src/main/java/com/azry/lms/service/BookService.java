package com.azry.lms.service;

import com.azry.lms.dto.response.BookResponse;

import java.util.List;

public interface BookService {
    List<BookResponse> searchBooks(String query);
}
