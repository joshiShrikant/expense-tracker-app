package com.jts.expensetracker.service.impl;

import com.jts.expensetracker.dto.DashboardSummary;
import com.jts.expensetracker.model.Expense;
import com.jts.expensetracker.repository.DashboardRepository;
import com.jts.expensetracker.service.DashboardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class DashboardServiceImpl implements DashboardService {

    @Autowired
    private DashboardRepository dashboardRepository;

//    for all expenses
    @Override
    public DashboardSummary getSummary() {
        List<Expense> expenses = dashboardRepository.findAllExpenses();

        // Calculate total expenses
        double totalExpenses = expenses.stream()
                .mapToDouble(e -> e.getExpenseAmount().doubleValue())
                .sum();
        // Calculate category totals
        Map<String, Double> totalByCategory = expenses.stream()
                .collect(Collectors.groupingBy(
                        Expense::getMainCategory,
                        Collectors.summingDouble(e -> e.getExpenseAmount().doubleValue())
                ));

        return new DashboardSummary(totalExpenses, totalByCategory);
    }

    // for user specific expenses
    public DashboardSummary getSummaryByUser() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        List<Expense> expenses = dashboardRepository.findExpensesByUsername(username);

        double totalExpenses = expenses.stream()
                .mapToDouble(e -> e.getExpenseAmount().doubleValue())
                .sum();

        Map<String, Double> totalByCategory = expenses.stream()
                .collect(Collectors.groupingBy(
                        Expense::getMainCategory,
                        Collectors.summingDouble(e -> e.getExpenseAmount().doubleValue())
                ));

        return new DashboardSummary(totalExpenses, totalByCategory);
    }
}