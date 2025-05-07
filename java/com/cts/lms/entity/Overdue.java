package com.cts.lms.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * Overdue class represents the overdue entity in the database.
 */
@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
public class Overdue {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long transactionId;
    private Long bookId;
    private String memberId;
    private LocalDate dueDate;
    private BigDecimal fineAmount;
    private String fineStatus; // Paid, Pending
    private String notificationStatus; // Sent, Not Sent
}
