package com.azry.lms.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class BookRequest {
    @NotBlank(message = "book title should be non-blank")
    private final String title;

    @NotBlank(message = "book author should be non-blank")
    private final String author;

    @NotBlank(message = "book isbn should be non-blank")
    private final String isbn;

}
