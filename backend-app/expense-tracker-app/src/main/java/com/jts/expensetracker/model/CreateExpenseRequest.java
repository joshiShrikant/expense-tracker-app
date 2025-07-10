package com.jts.expensetracker.model;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Setter
@Getter
public class CreateExpenseRequest {
    private String expenseName;
    private String expenseDate;
    private BigDecimal expenseAmount;
    private String mainCategory;
    private String subCategory;
    User user; // Assuming User is a class that represents the user making the expense
}
