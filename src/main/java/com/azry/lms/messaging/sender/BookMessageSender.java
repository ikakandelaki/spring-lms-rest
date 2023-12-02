package com.azry.lms.messaging.sender;

import com.azry.lms.messaging.model.BookMessage;
import com.azry.lms.util.constant.JmsConstant;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Component;

@Component
public class BookMessageSender {
    private final JmsTemplate jmsTemplate;

    public BookMessageSender(JmsTemplate jmsTemplate) {
        this.jmsTemplate = jmsTemplate;
    }

    public void sendBookMessage(BookMessage bookMessage) {
        jmsTemplate.convertAndSend(JmsConstant.QUEUE_NAME, bookMessage);
    }
}
