package com.jts.expensetracker.controller;

import com.jts.expensetracker.dto.ExpenseDto;
import com.jts.expensetracker.model.CreateExpenseRequest;
import com.jts.expensetracker.model.Expense;
import com.jts.expensetracker.service.ExpenseService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/expenses")
@RequiredArgsConstructor
@Tag(name = "Expenses", description = "Manage your expenses")
public class ExpenseController {

    private final ExpenseService expenseService;

    @PostMapping
    @Operation(summary = "Create a new expense")
    public ResponseEntity<ExpenseDto> addExpense(@RequestBody CreateExpenseRequest request) {
        ExpenseDto saved = expenseService.addExpense(request);
        return ResponseEntity.ok(saved);
    }

    @GetMapping
    @Operation(summary = "Get all expenses")
    public List<ExpenseDto> getAllExpenses() {
        return expenseService.getAllExpenses();
    }

    @GetMapping("/{userName}")
    @Operation(summary = "Get all expenses by username")
    public List<ExpenseDto> getExpenses(@PathVariable String userName) {
        return expenseService.getExpensesByUsername(userName);
    }

    @GetMapping("/{username}/{id}")
    @Operation(summary = "Get the expense by userName and Id")
    public ResponseEntity<Expense> getExpenseByUserAndId(@PathVariable String username, @PathVariable Long id) {
        Expense expense = expenseService.getExpenseByUserAndId(username, id);
        return ResponseEntity.ok(expense);
    }

    @PutMapping("/edit/{userName}/{id}")
    @Operation(summary = "Update the expense by userName and Id")
    public ResponseEntity<ExpenseDto> updateExpense(@PathVariable Long id, @RequestBody ExpenseDto updatedExpense) {
        ExpenseDto expense = expenseService.updateExpense(id, updatedExpense);
        return ResponseEntity.ok(expense);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete the expense by Id")
    public ResponseEntity<Map<String, String>> deleteExpense(@PathVariable Long id) {
        expenseService.deleteExpense(id);

        Map<String, String> response = new HashMap<>();
        response.put("message", "Expense deleted successfully");
        return ResponseEntity.ok(response);
    }

}