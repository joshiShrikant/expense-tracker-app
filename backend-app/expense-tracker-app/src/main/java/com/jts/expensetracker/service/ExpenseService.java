package com.jts.expensetracker.service;

import com.jts.expensetracker.dto.ExpenseDto;
import com.jts.expensetracker.model.CreateExpenseRequest;
import com.jts.expensetracker.model.Expense;

import java.util.List;

public interface ExpenseService {

    ExpenseDto addExpense(CreateExpenseRequest request);

    ExpenseDto updateExpense(Long id, ExpenseDto expenseDto);

    List<ExpenseDto> getExpensesByUsername(String username);

    Expense getExpenseByUserAndId(String username, Long id);

    List<ExpenseDto> getAllExpenses();

    void deleteExpense(Long id);

    List<Expense> getExpensesForCurrentUser();
}

