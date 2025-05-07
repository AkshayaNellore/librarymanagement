package com.cts.lms.service;

import com.cts.lms.entity.BorrowingTransaction;
import com.cts.lms.entity.Overdue;
import com.cts.lms.exception.HistoryNotFoundException;
import com.cts.lms.repository.BorrowingTransactionRepository;
import com.cts.lms.repository.OverdueRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;

/**
 * BorrowingTransactionService provides operations for managing borrowing transactions,
 * calculating overdue fines, and handling fine payments.
 */
@Service
public class BorrowingTransactionService {

    @Autowired
    private BorrowingTransactionRepository borrowingTransactionRepository;

    @Autowired
    private OverdueRepository overdueRepository;

    private static final BigDecimal FINE_PER_DAY = new BigDecimal("2.00");
    private static final int BORROW_DAYS_LIMIT = -1;

    /**
     * Marks overdue borrowing transactions and calculates fines for them.
     * For each overdue transaction, an Overdue record is created, and a notification is sent.
     */
    public void markOverdueAndCalculateFines() {
        List<BorrowingTransaction> transactions = borrowingTransactionRepository.findOverdueTransactions(LocalDate.now().minusDays(BORROW_DAYS_LIMIT));
        for (BorrowingTransaction transaction : transactions) {
            long overdueDays = ChronoUnit.DAYS.between(transaction.getBorrowDate().plusDays(BORROW_DAYS_LIMIT), LocalDate.now());
            if (overdueDays > 0) {
                BigDecimal fineAmount = FINE_PER_DAY.multiply(new BigDecimal(overdueDays));
                Overdue overdue = new Overdue();
                overdue.setTransactionId(transaction.getTransactionId());
                overdue.setBookId(transaction.getBookId());
                overdue.setMemberId(transaction.getMemberId());
                overdue.setDueDate(transaction.getBorrowDate().plusDays(BORROW_DAYS_LIMIT));
                overdue.setFineAmount(fineAmount);
                overdue.setFineStatus("Pending");
                overdue.setNotificationStatus("Not Sent");
                overdueRepository.save(overdue);
                sendNotification(overdue);
            }
        }
    }

    /**
     * Sends a notification for an overdue transaction.
     * Updates the notification status to "Sent" after sending the notification.
     *
     * @param overdue the Overdue record for which the notification is sent
     */

    public void sendNotification(Overdue overdue) {
        // Implement notification logic here
        overdue.setNotificationStatus("Sent");
        overdueRepository.save(overdue);
    }

    /**
     * Allows a member to pay a fine for a specific book.
     * Updates the fine status to "Paid" if the fine is successfully paid.
     *
     * @param memberId the ID of the member paying the fine
     * @param bookId   the ID of the book for which the fine is being paid
     * @return true if the fine is successfully paid, false otherwise
     */

    public boolean payFine(String memberId, Long bookId) {
        Overdue overdue = overdueRepository.findByMemberIdAndBookIdAndFineStatus(memberId, bookId, "Pending");
        if (overdue != null) {
            overdue.setFineStatus("Paid");
            overdueRepository.save(overdue);
            return true;
        }
        return false;
    }
    
    public Optional<List<BorrowingTransaction>> getHistorybyId(String memberId){
 	   
 	   Optional<List<BorrowingTransaction>> memberHistory = borrowingTransactionRepository.findByMemberId(memberId);
 	   
 	  if(!memberHistory.isPresent())
 	 	   throw new HistoryNotFoundException("history not found for the user "+memberId);

 	   return memberHistory;
 	   
 	   
    }
    
    public Optional<List<String>>getBorrowedBook(String memberId,String Borrow){
    	System.out.println("inside Service Class");
    	Optional<List<String>> booksHolding  =  borrowingTransactionRepository.findByStatus(memberId,Borrow);
    	System.out.println("after logic "+booksHolding);
    	return booksHolding;
    }
}
