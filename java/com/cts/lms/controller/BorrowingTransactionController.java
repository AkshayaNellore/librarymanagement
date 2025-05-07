package com.cts.lms.controller;

import com.cts.lms.dto.ApiResponse;
import com.cts.lms.entity.BorrowingTransaction;
import com.cts.lms.service.BorrowingTransactionService;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
// import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

/**
 * BorrowingTransactionController handles operations related to borrowing transactions,
 * such as marking overdue transactions and paying fines.
 */

@RestController
@RequestMapping("/api/borrowing")
public class BorrowingTransactionController {

    @Autowired
    private BorrowingTransactionService borrowingTransactionService;
    /**
     * Marks overdue borrowing transactions and calculates fines for them.
     * This endpoint is restricted to users with the LIBRARIAN authority.
     *
     * @return ResponseEntity containing a success message if the operation is successful.
     */
    @PostMapping("/mark-overdue")
    // @PreAuthorize("hasAuthority('LIBRARIAN')")
    public ResponseEntity<String> markOverdueAndCalculateFines() {
        borrowingTransactionService.markOverdueAndCalculateFines();
        return ResponseEntity.ok("Overdue transactions marked and fines calculated.");
    }
    /**
     * Allows a member to pay a fine for a specific book.
     *
     * @param memberId The ID of the member paying the fine.
     * @param bookId   The ID of the book for which the fine is being paid.
     * @return ResponseEntity containing a success message if the fine is paid successfully,
     *         or a failure message if the operation fails.
     */
    @PostMapping("/pay-fine")
    public ResponseEntity<String> payFine(@RequestParam String memberId, @RequestParam Long bookId) {
        boolean success = borrowingTransactionService.payFine(memberId, bookId);
        if (success) {
            return ResponseEntity.ok("Fine paid successfully.");
        } else {
            return ResponseEntity.badRequest().body("Failed to pay fine.");
        }
    }
    
    @GetMapping("/history/{memberId}")
    public ResponseEntity<?> getHistoryById(@PathVariable String memberId){
    	try {
    		Optional<List<BorrowingTransaction>> history = borrowingTransactionService.getHistorybyId(memberId);
    		return ResponseEntity.ok(history);
    	}
    	catch(Exception e){
    		return ResponseEntity.badRequest().body(e.getMessage());
    	}
    }
    
    @GetMapping("/holdingbook/{memberId}")
    public ResponseEntity<?>borrowBook(@PathVariable String memberId){
    	System.out.println("on Controller");
    	try {
    		Optional<List<String>> booksHaving=borrowingTransactionService.getBorrowedBook(memberId,"Borrowed");
    		return ResponseEntity.ok(booksHaving);
    	}
    	catch(Exception e){
    		return ResponseEntity.badRequest().body(e.getMessage());
    		}
    }
}
