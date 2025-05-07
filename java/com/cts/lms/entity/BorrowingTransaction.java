package com.cts.lms.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * BorrowingTransaction class represents the borrowing transaction entity in the database.
 */
@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
public class BorrowingTransaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long transactionId;

    private Long bookId;
    
    private String memberId;

    // Temporal annotation is not needed for LocalDate
    private LocalDate borrowDate;

    private LocalDate returnDate;
    
    private  LocalDate expectedDate;
    
    private String status;
    
    public void setExpectedDate(LocalDateTime now) {

    	this.expectedDate = now.toLocalDate().plusDays(2);
    }

    public void setBorrowDate(LocalDateTime now) {
		// TODO Auto-generated method stub
        this.borrowDate = now.toLocalDate();
	//	throw new UnsupportedOperationException("Unimplemented method 'setBorrowDate'");
	}
 
    public void setReturnDate(LocalDateTime now) {
        // TODO Auto-generated method stub
        this.returnDate = now.toLocalDate();
       // throw new UnsupportedOperationException("Unimplemented method 'setReturnDate'");
    }
}
