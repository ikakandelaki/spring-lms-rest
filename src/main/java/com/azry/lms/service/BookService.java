package com.azry.lms.service;

import com.azry.lms.dto.request.BookRequest;
import com.azry.lms.dto.response.BookResponse;

import java.util.List;

public interface BookService {
    List<BookResponse> searchBooks(String query);

    BookResponse addBook(BookRequest request);

    BookResponse editBook(Long id, BookRequest request);

    void deleteBook(Long id);
}
