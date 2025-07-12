package com.jts.expensetracker.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ExpenseDto {

    private Long expenseId;

    private String expenseName;

    private String expenseCategory;

    private String expenseDate;

    private BigDecimal expenseAmount;

    private String mainCategory;

    private String subCategory;
}
