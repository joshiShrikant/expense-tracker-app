package com.jts.expensetracker.repository;

import com.jts.expensetracker.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import com.jts.expensetracker.model.Expense;

import java.util.List;

public interface ExpenseRepository extends JpaRepository<Expense, Long> {

    List<Expense> findByUser(User user);

//    List<Expense> findByUserName(Long username);

    List<Expense> findByUserUsername(String username);
}
