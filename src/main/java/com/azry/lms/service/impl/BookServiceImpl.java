package com.azry.lms.service.impl;

import com.azry.lms.dto.request.BookRequest;
import com.azry.lms.dto.response.BookResponse;
import com.azry.lms.entity.Book;
import com.azry.lms.exception.ResourceNotFoundException;
import com.azry.lms.repository.BookRepository;
import com.azry.lms.service.BookService;
import com.azry.lms.util.MappingUtil;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

    @Override
    public BookResponse addBook(BookRequest request) {
        Book book = MappingUtil.map(request, Book.class);
        Book savedBook = bookRepository.save(book);
        return BookResponse.ofEntity(savedBook);
    }

    @Override
    @Transactional
    public BookResponse editBook(Long id, BookRequest request) {
        Book book = getBook(id);

        book.setTitle(request.getTitle());
        book.setAuthor(request.getAuthor());
        book.setIsbn(request.getIsbn());

        Book updatedBook = bookRepository.save(book);
        return BookResponse.ofEntity(updatedBook);
    }

    @Override
    @Transactional
    public void deleteBook(Long id) {
        Book book = getBook(id);
        bookRepository.delete(book);
    }

    private Book getBook(Long id) {
        return bookRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Book", "id", id));
    }
}
