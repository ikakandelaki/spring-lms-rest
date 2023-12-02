package com.azry.lms.exception;

import com.azry.lms.dto.response.ExceptionResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Map<String, List<String>> handleMethodArgumentNotValidException(MethodArgumentNotValidException exception) {
        log.error("Validation error occurred: {}", exception.getMessage(), exception);

        return exception.getBindingResult()
                .getAllErrors()
                .stream()
                .filter(objectError -> objectError instanceof FieldError)
                .map(objectError -> (FieldError) objectError)
                .filter(fieldError -> Objects.nonNull(fieldError.getDefaultMessage()))
                .collect(Collectors.toMap(
                        FieldError::getField,
                        fieldError -> new ArrayList<>(List.of(fieldError.getDefaultMessage())),
                        (oldList, newList) -> {
                            oldList.addAll(newList);
                            return oldList;
                        }));
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ExceptionResponse handleResourceNotFoundException(ResourceNotFoundException exception, WebRequest request) {
        log.error("{} not found with {}: '{}'",
                exception.getResourceName(),
                exception.getFieldName(),
                exception.getFieldValue(),
                exception);

        return getExceptionResponse(exception, request);
    }

    @ExceptionHandler(AccessDeniedException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public ExceptionResponse handleAccessDeniedException(AccessDeniedException exception, WebRequest request) {
        log.error("Access denied", exception);

        return getExceptionResponse(exception, request);
    }

    @ExceptionHandler(LmsException.class)
    public ResponseEntity<ExceptionResponse> handleLmsException(LmsException exception, WebRequest request) {
        log.error("App exception occurred: {}", exception.getMessage(), exception);

        return new ResponseEntity<>(getExceptionResponse(exception, request), exception.getStatus());
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ExceptionResponse handleException(Exception exception, WebRequest request) {
        log.error("Internal server error occurred: {}", exception.getMessage(), exception);

        return getExceptionResponse(exception, request);
    }

    private ExceptionResponse getExceptionResponse(Exception exception, WebRequest request) {
        return new ExceptionResponse(
                new Date(),
                exception.getMessage(),
                request.getDescription(false)
        );
    }
}
