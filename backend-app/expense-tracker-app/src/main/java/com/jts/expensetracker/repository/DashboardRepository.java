package com.jts.expensetracker.repository;


import com.jts.expensetracker.model.Expense;

import java.util.List;

public interface DashboardRepository {
    List<Expense> findAllExpenses();
}
