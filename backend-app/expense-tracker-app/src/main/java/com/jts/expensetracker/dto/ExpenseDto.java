package com.jts.expensetracker.dto;

import java.math.BigDecimal;

import jakarta.persistence.Column;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ExpenseDto {

	private Long id;

	private String expenseName;

	private String expenseCategory;

	private String expenseDate;

	private BigDecimal expenseAmount;

	private String mainCategory;

	private String subCategory;
}
