package com.cts.lms.service;
 
import com.cts.lms.dto.BookRequestDTO;
import com.cts.lms.dto.BookResponseDTO;
import com.cts.lms.entity.Book;
import com.cts.lms.exception.ResourceNotFoundException;
import com.cts.lms.repository.BookRepository;
import com.cts.lms.repository.BorrowingTransactionRepository;
import com.cts.lms.mapper.BookMapper;
import com.cts.lms.utils.IsbnFormatter;
import com.cts.lms.utils.ResetBookId;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
 
import java.util.ArrayList;
import java.util.List;
 
@Service
@Transactional
@AllArgsConstructor
public class BookService {
    @Autowired
    private final BookRepository bookRepository;
    @Autowired
    private final ResetBookId resetBookId;
    @Autowired
    private final BorrowingTransactionRepository borrowingTransactionRepository;
 
 
    public List<BookResponseDTO> getAllBooks() {
        List<Book> books = bookRepository.findAll();
        if (books.isEmpty()) {
            throw new ResourceNotFoundException("No books to view list");
        }
        return BookMapper.toDTOList(books);
    }
 
    public BookResponseDTO getBookById(Long bookId) {
        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new ResourceNotFoundException("Book ID " + bookId + " not found"));
        return BookMapper.toDTO(book);
    }
 
    public BookResponseDTO addBook(BookRequestDTO dto) {
        validateBook(dto);
 
        if (bookRepository.findByIsbn(dto.getIsbn()).isPresent()) {
            throw new IllegalArgumentException("Given ISBN exists. Please enter a different ISBN.");
        }
 
        Book book = BookMapper.toEntity(dto);
        book.setIsbn(IsbnFormatter.formatIsbn(book.getIsbn()));
        Book savedBook = bookRepository.save(book);
        return BookMapper.toDTO(savedBook);
    }
 
    public BookResponseDTO modifyBook(Long bookId, BookRequestDTO dto) {
        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new ResourceNotFoundException("Book ID " + bookId + " not found"));
 
        if (dto.getTitle() != null) book.setTitle(dto.getTitle());
        if (dto.getAuthor() != null) book.setAuthor(dto.getAuthor());
        if (dto.getGenre() != null) book.setGenre(dto.getGenre());
        if (dto.getIsbn() != null) book.setIsbn(IsbnFormatter.formatIsbn(dto.getIsbn()));
        if (dto.getYearPublished() != null) book.setYearPublished(dto.getYearPublished());
        if (dto.getAvailableCopies() != null && dto.getAvailableCopies() >= 0) {
            book.setAvailableCopies(dto.getAvailableCopies());
        }
 
        return BookMapper.toDTO(bookRepository.save(book));
    }
 
    public String deleteBook(Long bookId) {
        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new ResourceNotFoundException("Book ID " + bookId + " not found"));
        boolean isBookBorrowed = borrowingTransactionRepository.existsByBookIdAndStatus(bookId, "Borrowed");
        if (isBookBorrowed) {
            throw new IllegalArgumentException("Cannot delete book with ID " + bookId + " as it is currently borrowed.");
        }
        bookRepository.delete(book);
        if (bookRepository.count() == 0) {
            resetBookId.resetAutoIncrement();
        }
        return "Book with ID " + bookId + " has been deleted";
    }
 
    public List<BookResponseDTO> searchBooksByAuthor(String author) {
        List<Book> books = bookRepository.findByAuthorContainingIgnoreCase(author);
        if (books.isEmpty()) {
            throw new ResourceNotFoundException("No books found by author: " + author);
        }
        return BookMapper.toDTOList(books);
    }
 
    public List<BookResponseDTO> searchBooksByTitle(String title) {
        List<Book> books = bookRepository.findByTitleContainingIgnoreCase(title);
        if (books.isEmpty()) {
            throw new ResourceNotFoundException("No books found with title: " + title);
        }
        return BookMapper.toDTOList(books);
    }
 
    public List<BookResponseDTO> searchBooksByGenre(String genre) {
        List<Book> books = bookRepository.findByGenreContainingIgnoreCase(genre);
        if (books.isEmpty()) {
            throw new ResourceNotFoundException("No books found in genre: " + genre);
        }
        return BookMapper.toDTOList(books);
    }
    private void validateBook(BookRequestDTO dto) {
        List<String> errors = new ArrayList<>();
 
        if (dto.getTitle() == null || dto.getTitle().trim().isEmpty()) {
            errors.add("Title is required");
        }
        if (dto.getAuthor() == null || dto.getAuthor().trim().isEmpty()) {
            errors.add("Author is required");
        }
        if (dto.getGenre() == null || dto.getGenre().trim().isEmpty()) {
            errors.add("Genre is required");
        }
        if (dto.getIsbn() == null || dto.getIsbn().trim().isEmpty()) {
            errors.add("ISBN is required");
        } else if (!dto.getIsbn().matches("^[0-9]{10,13}$")) {
            errors.add("Invalid ISBN format. It must be 10 or 13 digits long.");
        }
        if (dto.getYearPublished() == null || dto.getYearPublished().getValue() < 0) {
            errors.add("Year of publication must be a valid positive number.");
        }
        if (dto.getAvailableCopies() == null || dto.getAvailableCopies() < 0) {
            errors.add("Invalid number of available copies");
        }
 
        if (!errors.isEmpty()) {
            throw new IllegalArgumentException(String.join("/n", errors));
        }
    }
}