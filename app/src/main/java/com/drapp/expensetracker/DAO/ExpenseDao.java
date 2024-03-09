package com.drapp.expensetracker.DAO;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.drapp.expensetracker.Entities.Expense;
import com.drapp.expensetracker.Entities.CategorySpending;

import java.util.List;

    @Dao
    public interface ExpenseDao {
        @Insert
        void insert(Expense expense);
        @Insert(onConflict = OnConflictStrategy.REPLACE)
        void insertAll(List<Expense> expenses);
        @Query("SELECT * FROM expenses")
        LiveData<List<Expense>> getAllExpenses();

        @Query("SELECT * FROM expenses WHERE category = :category AND date = :startDate")
        LiveData<List<Expense>> getFilteredExpenses(String category, String startDate);

        @Query("SELECT SUM(amount) FROM expenses")
        LiveData<Double> getTotalSpent();

        @Query("SELECT category, SUM(amount) AS totalSpent FROM expenses GROUP BY category")
        LiveData<List<CategorySpending>> getCategorySpending();
    }


