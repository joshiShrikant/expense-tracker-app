package com.jts.expensetracker.repository;

import com.jts.expensetracker.model.Expense;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class DashboardRepositoryImpl implements DashboardRepository {

    @Autowired
    private ExpenseRepository expenseRepository;

    @Override
    public List<Expense> findAllExpenses() {
        return expenseRepository.findAll();
    }
}