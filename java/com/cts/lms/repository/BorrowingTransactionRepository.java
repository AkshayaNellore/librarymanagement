package com.cts.lms.repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.cts.lms.entity.BorrowingTransaction;

public interface BorrowingTransactionRepository extends JpaRepository<BorrowingTransaction, Long> {

    Optional<BorrowingTransaction> findByMemberIdAndBookIdAndStatus(String memberId, Long bookId, String status);
    
//    @Query("SELECT bt FROM BorrowingTransaction bt WHERE bt.status = 'Borrowed' AND bt.borrowDate < :overdueDate")
//    List<BorrowingTransaction> findOverdueTransactions(@Param("overdueDate") LocalDate overdueDate);
    
    @Query("SELECT bt FROM BorrowingTransaction bt WHERE bt.memberId = :memberId")
    Optional<List<BorrowingTransaction>> findByMemberId(@Param("memberId") String memberId);
    
    @Query("SELECT bt FROM BorrowingTransaction bt WHERE bt.status = 'Borrowed' AND bt.borrowDate < :overdueDate")
    List<BorrowingTransaction> findOverdueTransactions(LocalDate minusDays);
    
    boolean existsByBookIdAndStatus(Long bookId, String status);
    
    @Query("SELECT bt.bookId FROM BorrowingTransaction bt WHERE bt.memberId = :memberId AND bt.status = :Borrowed")
    Optional<List<String>> findByStatus(@Param("memberId") String memberId, String Borrowed);
}