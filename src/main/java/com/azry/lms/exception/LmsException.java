package com.azry.lms.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public class LmsException extends RuntimeException {
    private final HttpStatus status;
    private final String message;
}
