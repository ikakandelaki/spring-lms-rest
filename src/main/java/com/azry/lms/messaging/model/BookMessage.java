package com.azry.lms.messaging.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
@AllArgsConstructor
public class BookMessage implements Serializable {
    private Long bookId;
    private Long userId;
}
