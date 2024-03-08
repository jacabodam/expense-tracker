package com.drapp.expensetracker.DAO;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import com.drapp.expensetracker.Entities.Expense;

@Database(entities = {Expense.class}, version = 1, exportSchema = false)
public abstract class ExpenseDatabase extends RoomDatabase {
    public abstract ExpenseDao expenseDao();
}
