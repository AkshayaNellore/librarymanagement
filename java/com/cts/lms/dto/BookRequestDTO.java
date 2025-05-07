package com.cts.lms.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
// import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Year;

// @Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BookRequestDTO {

    @NotBlank
    private String title;

    @NotBlank
    private String author;

    @NotBlank
    private String genre;

    @NotBlank
    private String isbn;

    @NotNull
    private Year yearPublished;

    @NotNull
    private Integer availableCopies;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    public Year getYearPublished() {
        return yearPublished;
    }

    public void setYearPublished(Year yearPublished) {
        this.yearPublished = yearPublished;
    }

    public Integer getAvailableCopies() {
        return availableCopies;
    }

    public void setAvailableCopies(Integer availableCopies) {
        this.availableCopies = availableCopies;
    }

    
}
