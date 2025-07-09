package com.jts.expensetracker.service;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.jts.expensetracker.dto.ExpenseDto;
import com.jts.expensetracker.model.Expense;
import com.jts.expensetracker.repository.ExpenseRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ExpenseService {
	
	private final ExpenseRepository expenseRepository;
	
	public Expense addExpense(ExpenseDto expenseDto) {
		Expense expense = mapFromDto(expenseDto);
		
		return expenseRepository.save(expense);
	}

	public Expense updateExpense(Long id, ExpenseDto expenseDto) {
		System.out.println("Updating expense with ID: " + id + expenseDto);
		if (expenseDto.getId() == null) {
			throw new IllegalArgumentException("Expense ID is required");
		}
		
		Expense savedExpense = expenseRepository.findById(expenseDto.getId())
				.orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, String.format("Cannot Find Expense by ID %s", expenseDto.getId())));
		
		Expense expense = mapFromDto(expenseDto);
		
		savedExpense.setExpenseName(expense.getExpenseName());
		savedExpense.setExpenseCategory(expense.getExpenseCategory());
		savedExpense.setExpenseAmount(expense.getExpenseAmount());
		expenseRepository.save(savedExpense);
		return savedExpense;
	}
	
	public ExpenseDto getExpense(Long id) {
		Expense expense = expenseRepository.findById(id)
				.orElseThrow(() -> new ExpenseNotFoundException(String.format("Cannot Find Expense by ID - %s", id)));
		return mapToDto(expense);
	}
	
	public List<ExpenseDto> getAllExpenses() {

		return expenseRepository.findAll().stream().map(this::mapToDto).toList();
	}
	
	public void deleteExpense(Long id) {
		expenseRepository.deleteById(id);
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
