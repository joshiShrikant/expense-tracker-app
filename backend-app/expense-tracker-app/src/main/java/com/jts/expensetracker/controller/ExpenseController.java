package com.jts.expensetracker.controller;

import com.jts.expensetracker.model.CreateExpenseRequest;
import com.jts.expensetracker.model.Expense;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.jts.expensetracker.dto.ExpenseDto;
import com.jts.expensetracker.service.ExpenseService;
import lombok.RequiredArgsConstructor;

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
		ExpenseDto  saved = expenseService.addExpense(request);
//		ExpenseDto dto = mapToDto(saved); // implement this or use a mapper like ModelMapper or MapStruct
		return ResponseEntity.ok(saved);
	}

	@PutMapping("/{userName}/{id}")
	@Operation(summary = "Update the expense by Id")
	public ResponseEntity<Expense> updateExpense(@PathVariable Long id, @RequestBody ExpenseDto updatedExpense) {
		Expense expense = expenseService.updateExpense(id, updatedExpense);
		return ResponseEntity.ok(expense);
	}


	@GetMapping
	@Operation(summary = "Get all expenses")
	public List<ExpenseDto> getAllExpenses() {
		return expenseService.getAllExpenses();
	}

	@GetMapping("/{userName}")
	@Operation(summary = "Get the expense by username")
	public List<ExpenseDto> getExpense(@PathVariable String userName) {
		return expenseService.getExpensesByUsername(userName);
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