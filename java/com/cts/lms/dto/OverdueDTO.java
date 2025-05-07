package com.cts.lms.dto;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * OverdueDTO class is a Data Transfer Object for transferring overdue transaction data.
 */
@Data
public class OverdueDTO {

    private Long memberId;
    private LocalDate dueDate;
    private BigDecimal amount;
    private String status;
}
