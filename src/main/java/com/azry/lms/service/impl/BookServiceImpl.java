package com.azry.lms.service.impl;

import com.azry.lms.dto.response.BookResponse;
import com.azry.lms.repository.BookRepository;
import com.azry.lms.service.BookService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class BookServiceImpl implements BookService {
    private final BookRepository bookRepository;

    public BookServiceImpl(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    @Override
    public List<BookResponse> searchBooks(String query) {
        return bookRepository.searchBooks(query)
                .stream()
                .map(BookResponse::ofEntity)
                .collect(Collectors.toList());
    }
}
