package com.azry.lms.controller;

import com.azry.lms.dto.request.BookRequest;
import com.azry.lms.dto.response.BookResponse;
import com.azry.lms.messaging.model.BookMessage;
import com.azry.lms.messaging.sender.BookMessageSender;
import com.azry.lms.service.BookService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/books")
public class BookController {
    private final BookService bookService;
    private final BookMessageSender bookMessageSender;

    public BookController(BookService bookService, BookMessageSender bookMessageSender) {
        this.bookService = bookService;
        this.bookMessageSender = bookMessageSender;
    }

    @GetMapping
    public List<BookResponse> searchBooks(@RequestParam(required = false, defaultValue = "") String query) {
        return bookService.searchBooks(query);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public BookResponse addBook(@RequestBody @Valid BookRequest request) {
        return bookService.addBook(request);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public BookResponse editBook(@PathVariable Long id, @RequestBody @Valid BookRequest request) {
        return bookService.editBook(id, request);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public void deleteBook(@PathVariable Long id) {
        bookService.deleteBook(id);
    }

    @PutMapping("/{id}/borrow")
    public void borrowBook(@PathVariable Long id) {
        BookMessage bookMessage = new BookMessage(id, BookMessage.MessageType.BORROW);
        bookMessageSender.sendBookMessage(bookMessage);
    }

    @PutMapping("/{id}/return")
    public void returnBook(@PathVariable Long id) {
        BookMessage bookMessage = new BookMessage(id, BookMessage.MessageType.RETURN);
        bookMessageSender.sendBookMessage(bookMessage);
    }
}
