package com.azry.lms.repository;

import com.azry.lms.entity.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface BookRepository extends JpaRepository<Book, Long> {

    @Query("""
            select b from Book b
            where b.title like concat('%', :query, '%')
            or b.author like concat('%', :query, '%')
            or b.isbn like concat('%', :query, '%')""")
    List<Book> searchBooks(String query);
}
