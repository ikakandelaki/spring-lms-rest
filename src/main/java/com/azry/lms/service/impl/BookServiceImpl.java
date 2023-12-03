package com.azry.lms.service.impl;

import com.azry.lms.dto.request.BookRequest;
import com.azry.lms.dto.response.BookResponse;
import com.azry.lms.entity.Book;
import com.azry.lms.entity.User;
import com.azry.lms.entity.model.Status;
import com.azry.lms.exception.LmsException;
import com.azry.lms.exception.ResourceNotFoundException;
import com.azry.lms.messaging.model.BookMessage;
import com.azry.lms.repository.BookRepository;
import com.azry.lms.repository.UserRepository;
import com.azry.lms.service.BookService;
import com.azry.lms.util.MappingUtil;
import com.azry.lms.util.constant.JmsConstant;
import org.springframework.http.HttpStatus;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class BookServiceImpl implements BookService {
    private final JmsTemplate jmsTemplate;
    private final BookRepository bookRepository;
    private final UserRepository userRepository;

    public BookServiceImpl(
            JmsTemplate jmsTemplate,
            BookRepository bookRepository,
            UserRepository userRepository
    ) {
        this.jmsTemplate = jmsTemplate;
        this.bookRepository = bookRepository;
        this.userRepository = userRepository;
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

    @Override
    @Transactional
    public BookResponse borrowBook(Long id) {
        Book book = getBook(id);
        if (Status.BORROWED.getName().equals(book.getStatus())) {
            throw new LmsException(HttpStatus.BAD_REQUEST, String.format("Book with id '%s' is already borrowed", id));
        }

        User user = getUser();
        book.setStatus(Status.BORROWED.getName());
        book.setUser(user);
        Book borrowedBook = bookRepository.save(book);

        jmsTemplate.convertAndSend(JmsConstant.LMS_BORROW_QUEUE, new BookMessage(book.getId(), user.getId()));
        return BookResponse.ofEntity(borrowedBook);
    }

    @Override
    @Transactional
    public BookResponse returnBook(Long id) {
        Book book = getBook(id);
        if (Status.AVAILABLE.getName().equals(book.getStatus())) {
            throw new LmsException(HttpStatus.BAD_REQUEST, String.format("Book with id '%s' is not borrowed", id));
        }

        User user = getUser();
        if (!book.getUser().getId().equals(user.getId())) {
            throw new LmsException(HttpStatus.BAD_REQUEST,
                    String.format("Book with id '%s' is not borrowed by user with id '%s'", id, user.getId()));
        }
        book.setStatus(Status.AVAILABLE.getName());
        book.setUser(null);
        Book returnedBook = bookRepository.save(book);

        jmsTemplate.convertAndSend(JmsConstant.LMS_RETURN_QUEUE, new BookMessage(book.getId(), user.getId()));
        return BookResponse.ofEntity(returnedBook);
    }

    private Book getBook(Long id) {
        return bookRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Book", "id", id));
    }

    private User getUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();

        return userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("User", "username", username));
    }
}
