package com.cts.lms.mapper;

import com.cts.lms.dto.BookRequestDTO;
import com.cts.lms.dto.BookResponseDTO;
import com.cts.lms.entity.Book;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

@Component
public class BookMapper {

    public static BookResponseDTO toDTO(Book book) {
        return BookResponseDTO.builder()
                .bookId(book.getBookId())
                .title(book.getTitle())
                .author(book.getAuthor())
                .genre(book.getGenre())
                .isbn(book.getIsbn())
                .yearPublished(book.getYearPublished())
                .availableCopies(book.getAvailableCopies())
                .build();
    }

    public static Book toEntity(BookRequestDTO dto) {
        Book book = new Book();
        book.setTitle(dto.getTitle());
        book.setAuthor(dto.getAuthor());
        book.setGenre(dto.getGenre());
        book.setIsbn(dto.getIsbn());
        book.setYearPublished(dto.getYearPublished());
        book.setAvailableCopies(dto.getAvailableCopies());
        return book;
    }

    public static List<BookResponseDTO> toDTOList(List<Book> books) {
        return books.stream()
                .map(BookMapper::toDTO)
                .collect(Collectors.toList());
    }
}
