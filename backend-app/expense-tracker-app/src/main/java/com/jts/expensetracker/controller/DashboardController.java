package com.jts.expensetracker.controller;

import com.jts.expensetracker.dto.DashboardSummary;
import com.jts.expensetracker.dto.ExpenseDto;
import com.jts.expensetracker.service.ExpenseService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/expenses/dashboard")
@RequiredArgsConstructor
@Tag(name = "Dashboard", description = "Manage your dashboard")
public class DashboardController {

    private final ExpenseService expenseService;

    @GetMapping
    public DashboardSummary getDashboardSummary() {
        List<ExpenseDto> expenses = expenseService.getAllExpenses();

        // Convert total to double
        double totalExpenses = expenses.stream()
                .map(ExpenseDto::getExpenseAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add)
                .doubleValue();

        Map<String, Double> categoryTotals = expenses.stream()
                .filter(e -> e.getMainCategory() != null && e.getSubCategory() != null)
                .collect(Collectors.groupingBy(
                        e -> e.getMainCategory() + " > " + e.getSubCategory(),
                        Collectors.reducing(
                                BigDecimal.ZERO,
                                ExpenseDto::getExpenseAmount,
                                BigDecimal::add
                        )
                ))
                .entrySet().stream()
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        e -> e.getValue().doubleValue()
                ));

        return new DashboardSummary(totalExpenses, categoryTotals);
    }
}
