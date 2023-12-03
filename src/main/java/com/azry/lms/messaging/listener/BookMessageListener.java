package com.azry.lms.messaging.listener;

import com.azry.lms.messaging.model.BookMessage;
import com.azry.lms.util.constant.JmsConstant;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class BookMessageListener {

    @JmsListener(destination = JmsConstant.LMS_BORROW_QUEUE)
    public void receiveBorrowMessage(BookMessage bookMessage) {
        log.info("Book with id '{}' borrowed by user with id '{}'", bookMessage.getBookId(), bookMessage.getUserId());
    }

    @JmsListener(destination = JmsConstant.LMS_RETURN_QUEUE)
    public void receiveReturnMessage(BookMessage bookMessage) {
        log.info("Book with id '{}' returned by user with id '{}'", bookMessage.getBookId(), bookMessage.getUserId());
    }
}
