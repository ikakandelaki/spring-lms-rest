package com.azry.lms.dto.response;

import com.azry.lms.entity.Book;
import com.azry.lms.util.MappingUtil;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BookResponse {
    private Long id;
    private String title;
    private String author;
    private String isbn;
    private String status;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private UserResponse user;

    public static BookResponse ofEntity(Book book) {
        return MappingUtil.map(book, BookResponse.class);
    }
}
