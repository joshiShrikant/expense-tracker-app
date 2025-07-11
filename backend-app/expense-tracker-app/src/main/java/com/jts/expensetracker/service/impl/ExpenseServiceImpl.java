package com.jts.expensetracker.service.impl;

import com.jts.expensetracker.dto.ExpenseDto;
import com.jts.expensetracker.model.CreateExpenseRequest;
import com.jts.expensetracker.model.Expense;
import com.jts.expensetracker.model.User;
import com.jts.expensetracker.repository.ExpenseRepository;
import com.jts.expensetracker.repository.UserRepository;
import com.jts.expensetracker.service.ExpenseService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
@RequiredArgsConstructor

public class ExpenseServiceImpl implements ExpenseService {

    private final ExpenseRepository expenseRepository;
    private final UserRepository userRepository;

    @Override
    public ExpenseDto addExpense(CreateExpenseRequest request) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        request.setUser(user);

        Expense newExpense = new Expense();
        newExpense.setExpenseName(request.getExpenseName());
        newExpense.setExpenseAmount(request.getExpenseAmount());
        newExpense.setExpenseDate(request.getExpenseDate());
        newExpense.setMainCategory(request.getMainCategory());
        newExpense.setSubCategory(request.getSubCategory());
        newExpense.setUser(user);

        if (newExpense.getExpenseAmount() == null
                || newExpense.getMainCategory() == null
                || newExpense.getSubCategory() == null) {
            throw new IllegalArgumentException("Amount and Category are required");
        }

        Expense savedExpense = expenseRepository.save(newExpense);
        return mapToDto(savedExpense);
    }

    @Override
    public Expense updateExpense(Long id, ExpenseDto expenseDto) {
        if (expenseDto.getId() == null) {
            throw new IllegalArgumentException("Expense ID is required");
        }

        Expense savedExpense = expenseRepository.findById(expenseDto.getId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, String.format("Cannot Find Expense by ID %s", expenseDto.getId())));

        Expense expense = mapFromDto(expenseDto);

        savedExpense.setExpenseName(expense.getExpenseName());
        savedExpense.setMainCategory(expense.getMainCategory());
        savedExpense.setSubCategory(expense.getSubCategory());
        savedExpense.setExpenseAmount(expense.getExpenseAmount());

        return expenseRepository.save(savedExpense);
    }

    @Override
    public List<ExpenseDto> getExpensesByUsername(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        List<Expense> expenses = expenseRepository.findByUserUsername(user.getUsername());
        return expenses.stream().map(this::mapToDto).toList();
    }

    @Override
    public List<ExpenseDto> getAllExpenses() {
        return expenseRepository.findAll().stream().map(this::mapToDto).toList();
    }

    @Override
    public void deleteExpense(Long id) {
        expenseRepository.deleteById(id);
    }

    @Override
    public List<Expense> getExpensesForCurrentUser() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        return expenseRepository.findByUser(user);
    }

    private Expense mapFromDto(ExpenseDto expense) {
        return Expense.builder()
                .expenseName(expense.getExpenseName())
                .mainCategory(expense.getMainCategory())
                .subCategory(expense.getSubCategory())
                .expenseAmount(expense.getExpenseAmount())
                .expenseDate(expense.getExpenseDate())
                .build();
    }

    private ExpenseDto mapToDto(Expense expense) {
        return ExpenseDto.builder()
                .id(expense.getId())
                .expenseName(expense.getExpenseName())
                .mainCategory(expense.getMainCategory())
                .subCategory(expense.getSubCategory())
                .expenseAmount(expense.getExpenseAmount())
                .expenseDate(expense.getExpenseDate())
                .build();
    }
}