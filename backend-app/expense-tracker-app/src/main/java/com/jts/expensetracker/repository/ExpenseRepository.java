package com.jts.expensetracker.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.jts.expensetracker.model.Expense;

public interface ExpenseRepository extends JpaRepository<Expense, Long> {
}
