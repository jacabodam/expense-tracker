package com.drapp.expensetracker;


import android.app.Application;

import androidx.room.Room;

import com.drapp.expensetracker.DAO.ExpenseDatabase;

public class MyApp extends Application {
    private static ExpenseDatabase expenseDatabase;

    @Override
    public void onCreate() {
        super.onCreate();
        expenseDatabase = Room.databaseBuilder(getApplicationContext(),
                        ExpenseDatabase.class, "expense_database")
                .build();
    }

    public static ExpenseDatabase getExpenseDatabase() {
        return expenseDatabase;
    }
}
