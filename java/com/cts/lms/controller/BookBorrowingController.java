package com.cts.lms.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cts.lms.entity.BorrowingRequest;
import com.cts.lms.service.BookBorrowingService;

@RestController
@RequestMapping("/lms/book/borrow")
@CrossOrigin
public class BookBorrowingController {

    @Autowired
    private BookBorrowingService bookBorrowingService;

    @PostMapping("/borrow")
    public ResponseEntity<String> borrowBook(@RequestBody BorrowingRequest request) {
        boolean success = bookBorrowingService.borrowBook(request.getMemberId(), request.getBookId());
        if (success) {
            return ResponseEntity.ok("Book borrowed successfully.");
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Failed to borrow book.");
        }
    }

    @PostMapping("/return")
    public ResponseEntity<String> returnBook(@RequestBody BorrowingRequest request) {
        boolean success = bookBorrowingService.returnBook(request.getMemberId(), request.getBookId());
        if (success) {
            return ResponseEntity.ok("Book returned successfully.");
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Failed to return book.");
        }
    }
    
    
}
