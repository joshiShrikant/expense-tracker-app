package com.jts.expensetracker.dto;

import java.util.Map;

public class DashboardSummary {
    private Double totalExpenses;
    private Map<String, Double> categoryTotals;

    public DashboardSummary(Double totalExpenses, Map<String, Double> categoryTotals) {
        this.totalExpenses = totalExpenses;
        this.categoryTotals = categoryTotals;
    }

    public Double getTotalExpenses() {
        return totalExpenses;
    }

    public Map<String, Double> getCategoryTotals() {
        return categoryTotals;
    }
}
