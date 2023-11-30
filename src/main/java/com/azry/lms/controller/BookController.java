package com.azry.lms.controller;

import com.azry.lms.dto.response.BookResponse;
import com.azry.lms.service.BookService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/books")
public class BookController {
    private final BookService bookService;

    public BookController(BookService bookService) {
        this.bookService = bookService;
    }

    @GetMapping
    public List<BookResponse> searchBooks(@RequestParam(required = false, defaultValue = "") String query) {
        return bookService.searchBooks(query);
    }
}
