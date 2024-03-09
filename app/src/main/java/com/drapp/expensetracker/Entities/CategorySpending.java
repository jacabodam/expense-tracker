package com.drapp.expensetracker.Entities;

import androidx.room.ColumnInfo;

public class CategorySpending {

    @ColumnInfo(name = "category")
    private String category;

    @ColumnInfo(name = "totalSpent")
    private double totalSpent;

    public CategorySpending(String category, double totalSpent) {
        this.category = category;
        this.totalSpent = totalSpent;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public double getTotalSpent() {
        return totalSpent;
    }

    public void setTotalSpent(double totalSpent) {
        this.totalSpent = totalSpent;
    }
}
