package com.drapp.expensetracker.Entities;
import android.app.Application;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import com.drapp.expensetracker.Entities.Expense;
import com.drapp.expensetracker.DAO.ExpenseDatabase;
import com.drapp.expensetracker.MyApp;

import java.util.List;

public class ExpenseViewModel extends AndroidViewModel {

    private ExpenseDatabase expenseDatabase;
    private LiveData<List<Expense>> allExpenses;

    public ExpenseViewModel(@NonNull Application application) {
        super(application);
        expenseDatabase = MyApp.getExpenseDatabase(); // Assuming MyApp has a method to access the database
        allExpenses = expenseDatabase.expenseDao().getAllExpenses();
    }

    public LiveData<List<Expense>> getAllExpenses() {
        return allExpenses;
    }

    public LiveData<List<Expense>> getFilteredExpenses(String category, String startDate) {
        return expenseDatabase.expenseDao().getFilteredExpenses(category, startDate);
    }
}
