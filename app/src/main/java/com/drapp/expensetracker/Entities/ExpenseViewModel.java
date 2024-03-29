package com.drapp.expensetracker.Entities;
import android.app.Application;
import android.os.AsyncTask;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import com.drapp.expensetracker.DAO.ExpenseDatabase;
import com.drapp.expensetracker.MyApp;

import java.util.List;

public class ExpenseViewModel extends AndroidViewModel {

    private ExpenseDatabase expenseDatabase;
    private LiveData<List<Expense>> allExpenses;
    private LiveData<Double> totalSpent;
    private LiveData<List<CategorySpending>> categorySpending;
    public ExpenseViewModel(@NonNull Application application) {
        super(application);
        expenseDatabase = MyApp.getExpenseDatabase(); // Assuming MyApp has a method to access the database
        allExpenses = expenseDatabase.expenseDao().getAllExpenses();
        totalSpent = expenseDatabase.expenseDao().getTotalSpent();
        categorySpending = expenseDatabase.expenseDao().getCategorySpending();
    }

    public LiveData<List<Expense>> getAllExpenses() {
        return allExpenses;
    }

    public LiveData<List<Expense>> getFilteredExpenses(String category, String startDate) {
        return expenseDatabase.expenseDao().getFilteredExpenses(category, startDate);
    }

    public LiveData<Double> getTotalSpent() {
        return totalSpent;
    }

    public LiveData<List<CategorySpending>> getCategorySpending() {
        return categorySpending;
    }

    public void insertAll(List<Expense> expenses) {
        new InsertAllAsyncTask(expenseDatabase).execute(expenses);
    }

    private static class InsertAllAsyncTask extends AsyncTask<List<Expense>, Void, Void> {
        private ExpenseDatabase expenseDatabase;

        InsertAllAsyncTask(ExpenseDatabase expenseDatabase) {
            this.expenseDatabase = expenseDatabase;
        }

        @Override
        protected Void doInBackground(List<Expense>... lists) {
            expenseDatabase.expenseDao().insertAll(lists[0]);
            return null;
        }
    }
}
