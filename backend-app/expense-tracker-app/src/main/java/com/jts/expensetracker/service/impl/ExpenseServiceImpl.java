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
    public ExpenseDto updateExpense(Long id, ExpenseDto expenseDto) {
        expenseDto.setExpenseId(id);
        if (expenseDto.getExpenseId() == null) {
            throw new IllegalArgumentException("Expense ID is required");
        }

        Expense oldExpense = expenseRepository.findById(expenseDto.getExpenseId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, String.format("Cannot Find Expense by ID %s", expenseDto.getExpenseId())));

        Expense expense = mapFromDto(expenseDto);

        oldExpense.setExpenseName(expense.getExpenseName());
        oldExpense.setMainCategory(expense.getMainCategory());
        oldExpense.setSubCategory(expense.getSubCategory());
        oldExpense.setExpenseAmount(expense.getExpenseAmount());
        oldExpense.setExpenseDate(expense.getExpenseDate());

        Expense savedExpense = expenseRepository.save(oldExpense);

        return ExpenseDto.builder()
                .expenseId(savedExpense.getExpenseId())
                .expenseName(savedExpense.getExpenseName())
                .mainCategory(savedExpense.getMainCategory())
                .subCategory(savedExpense.getSubCategory())
                .expenseAmount(savedExpense.getExpenseAmount())
                .expenseDate(savedExpense.getExpenseDate())
                .build();
    }

    @Override
    public List<ExpenseDto> getExpensesByUsername(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        List<Expense> expenses = expenseRepository.findByUserUsername(user.getUsername());
        return expenses.stream().map(this::mapToDto).toList();
    }


    @Override
    public Expense getExpenseByUserAndId(String username, Long id) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        Expense expense = expenseRepository.findByUserUsernameAndExpenseId(user.getUsername(), id);

        if (expense != null) {
            return expense;
        } else {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND,
                    String.format("Expense with ID %s not found for user %s", id, username)
            );
        }
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
                .expenseId(expense.getExpenseId())
                .expenseName(expense.getExpenseName())
                .mainCategory(expense.getMainCategory())
                .subCategory(expense.getSubCategory())
                .expenseAmount(expense.getExpenseAmount())
                .expenseDate(expense.getExpenseDate())
                .build();
    }
}