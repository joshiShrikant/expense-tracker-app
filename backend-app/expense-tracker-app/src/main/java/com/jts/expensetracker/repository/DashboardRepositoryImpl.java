package com.jts.expensetracker.repository;

import com.jts.expensetracker.model.Expense;
import com.jts.expensetracker.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class DashboardRepositoryImpl implements DashboardRepository {

    @Autowired
    private ExpenseRepository expenseRepository;

    @Autowired
    private UserRepository userRepository;

    @Override
    public List<Expense> findAllExpenses() {
        return expenseRepository.findAll();
    }


    @Override
    public List<Expense> findExpensesByUsername(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));
        return expenseRepository.findByUser(user);
    }
}