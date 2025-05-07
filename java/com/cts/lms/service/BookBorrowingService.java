package com.cts.lms.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cts.lms.entity.Book;
import com.cts.lms.entity.BorrowingTransaction;
import com.cts.lms.exception.HistoryNotFoundException;
import com.cts.lms.repository.BookRepository;
import com.cts.lms.repository.BorrowingTransactionRepository;

@Service
public class BookBorrowingService {

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private BorrowingTransactionRepository borrowingTransactionRepository;

    public boolean borrowBook(String memberId, Long bookId) {
        Optional<Book> bookOpt = bookRepository.findById(bookId);
        if (bookOpt.isPresent() && bookOpt.get().getAvailableCopies() > 0) {
            Book book = bookOpt.get();
            book.setAvailableCopies(book.getAvailableCopies() - 1);
            bookRepository.save(book);

            BorrowingTransaction transaction = new BorrowingTransaction();
            transaction.setBookId(bookId);
            transaction.setMemberId(memberId);
            transaction.setBorrowDate(LocalDateTime.now());
            transaction.setStatus("Borrowed");
            transaction.setExpectedDate(LocalDateTime.now());
            borrowingTransactionRepository.save(transaction);

            return true;
        }
        return false;
    }

    public boolean returnBook(String memberId, Long bookId) {
        Optional<BorrowingTransaction> transactionOpt = borrowingTransactionRepository
                .findByMemberIdAndBookIdAndStatus(memberId, bookId, "Borrowed");
        if (transactionOpt.isPresent()) {
            BorrowingTransaction transaction = transactionOpt.get();
            transaction.setReturnDate(LocalDateTime.now());
            transaction.setStatus("Returned");
            borrowingTransactionRepository.save(transaction);

            Optional<Book> bookOpt = bookRepository.findById(bookId);
            if (bookOpt.isPresent()) {
                Book book = bookOpt.get();
                book.setAvailableCopies(book.getAvailableCopies() + 1);
                bookRepository.save(book);
            }

            return true;
        }
        return false;
    }
  
}