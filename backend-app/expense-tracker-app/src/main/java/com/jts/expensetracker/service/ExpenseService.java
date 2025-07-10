package com.jts.expensetracker.service;

import java.util.List;

import com.jts.expensetracker.model.CreateExpenseRequest;
import com.jts.expensetracker.model.User;
import com.jts.expensetracker.repository.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
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

	private final UserRepository userRepository;


	public ExpenseDto addExpense(CreateExpenseRequest request) {
		// Step 1: Get current logged-in user
		String username = SecurityContextHolder.getContext().getAuthentication().getName();
		User user = userRepository.findByUsername(username)
				.orElseThrow(() -> new UsernameNotFoundException("User not found"));

		// Step 2: Link user to expense
		request.setUser(user);

		// decouple the frontend model from Expense entity.
		Expense newExpense = new Expense();
		newExpense.setExpenseName(request.getExpenseName());
		newExpense.setExpenseAmount(request.getExpenseAmount());
		newExpense.setExpenseDate(request.getExpenseDate());
		newExpense.setMainCategory(request.getMainCategory());
		newExpense.setSubCategory(request.getSubCategory());
		// Set the user for the expense
		newExpense.setUser(user);


		// Validate required fields
		if (newExpense.getExpenseAmount() == null
				|| newExpense.getMainCategory() == null
				|| newExpense.getSubCategory() == null) {
			throw new IllegalArgumentException("Amount and Category are required");
		}

		// Step 3: Save and convert to DTO
		Expense savedExpense = expenseRepository.save(newExpense);
		return mapToDto(savedExpense);
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
		savedExpense.setMainCategory(expense.getMainCategory());
		savedExpense.setSubCategory(expense.getSubCategory());
		savedExpense.setExpenseAmount(expense.getExpenseAmount());
		expenseRepository.save(savedExpense);
		return savedExpense;
	}
	
	public List<ExpenseDto> getExpensesByUsername(String username) {
		User user = userRepository.findByUsername(username)
				.orElseThrow(() -> new UsernameNotFoundException("User not found"));

		List<Expense> expenses = expenseRepository.findByUserUsername(user.getUsername());
		return expenses.stream().map(this::mapToDto).toList();
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

	public List<Expense> getExpensesForCurrentUser() {
		// Step 1: Get the current logged-in user's username/email
		String username = SecurityContextHolder.getContext().getAuthentication().getName();

		// Step 2: Fetch the User entity from the DB
		User user = userRepository.findByUsername(username)
				.orElseThrow(() -> new UsernameNotFoundException("User not found"));

		// Step 3: Return only this user's expenses
		return expenseRepository.findByUser(user);
	}



	
}
