package com.cts.lms.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cts.lms.dto.BookRequestDTO;
import com.cts.lms.dto.BookResponseDTO;
import com.cts.lms.service.BookService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/books")
@RequiredArgsConstructor
@CrossOrigin
public class BookController {
    @Autowired
    private BookService bookService;

    @GetMapping("/view-all")
    public ResponseEntity<List<BookResponseDTO>> getAllBooks() {
        System.out.println("Fetching all books");
        List<BookResponseDTO> books = bookService.getAllBooks();
        return new ResponseEntity<>(books, HttpStatus.OK);
    }

    @GetMapping("/view/{bookId}")
    public ResponseEntity<BookResponseDTO> getBookById(@PathVariable Long bookId) {
        BookResponseDTO book = bookService.getBookById(bookId);
        return new ResponseEntity<>(book, HttpStatus.OK);
    }

    @PostMapping("/add")
    public ResponseEntity<BookResponseDTO> addBook(@RequestBody BookRequestDTO bookRequestDTO) {
        BookResponseDTO created = bookService.addBook(bookRequestDTO);
        return new ResponseEntity<>(created, HttpStatus.CREATED);
    }

    @PatchMapping("/update/{bookId}")
    public ResponseEntity<BookResponseDTO> modifyBook(@PathVariable Long bookId,
                                                      @RequestBody BookRequestDTO bookRequestDTO) {
        BookResponseDTO updated = bookService.modifyBook(bookId, bookRequestDTO);
        return new ResponseEntity<>(updated, HttpStatus.OK);
    }

    @DeleteMapping("/delete/{bookId}")
    public ResponseEntity<String> deleteBook(@PathVariable Long bookId) {
        String result = bookService.deleteBook(bookId);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @GetMapping("/search/author/{author}")
    public ResponseEntity<List<BookResponseDTO>> searchBooksByAuthor(@PathVariable String author) {
        List<BookResponseDTO> books = bookService.searchBooksByAuthor(author);
        return new ResponseEntity<>(books, HttpStatus.OK);
    }

    @GetMapping("/search/title/{title}")
    public ResponseEntity<List<BookResponseDTO>> searchBooksByTitle(@PathVariable String title) {
        List<BookResponseDTO> books = bookService.searchBooksByTitle(title);
        return new ResponseEntity<>(books, HttpStatus.OK);
    }

    @GetMapping("/search/genre/{genre}")
    public ResponseEntity<List<BookResponseDTO>> searchBooksByGenre(@PathVariable String genre) {
        List<BookResponseDTO> books = bookService.searchBooksByGenre(genre);
        return new ResponseEntity<>(books, HttpStatus.OK);
    }
}
