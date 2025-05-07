package com.cts.lms.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Year;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BookResponseDTO {

    private Long bookId;
    private String title;
    private String author;
    private String genre;
    private String isbn;
    private Year yearPublished;
    private int availableCopies;
}
