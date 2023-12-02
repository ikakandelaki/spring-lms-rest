package com.azry.lms.dto.request;

import com.azry.lms.util.RegexConstant;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class BookRequest {
    @NotBlank(message = "book title should be non-blank")
    private String title;

    @NotBlank(message = "book author should be non-blank")
    private String author;

    @NotBlank(message = "book isbn should be non-blank")
    @Pattern(regexp = RegexConstant.ISBN_REGEX, message = "book isbn should be valid")
    private String isbn;

}
