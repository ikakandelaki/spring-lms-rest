package com.azry.lms.messaging.listener;

import com.azry.lms.messaging.model.BookMessage;
import com.azry.lms.service.BookService;
import com.azry.lms.util.constant.JmsConstant;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

@Component
public class BookMessageListener {
    private final BookService bookService;

    public BookMessageListener(BookService bookService) {
        this.bookService = bookService;
    }

    @JmsListener(destination = JmsConstant.QUEUE_NAME)
    public void handleBookMessage(BookMessage bookMessage) {
        // TODO: 12/3/2023 use bookService and log activity
    }
}
