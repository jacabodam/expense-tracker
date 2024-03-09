package com.drapp.expensetracker.Activities;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.drapp.expensetracker.Adapters.CategorySpendingAdapter;
import com.drapp.expensetracker.Entities.CategorySpending;
import com.drapp.expensetracker.Entities.ExpenseViewModel;
import com.drapp.expensetracker.R;

import java.util.ArrayList;
import java.util.List;

public class ReportActivity extends AppCompatActivity {

    private TextView textTotalSpent;
    private RecyclerView recyclerViewCategorySpending;
    private CategorySpendingAdapter categorySpendingAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report);

        // Initialize views
        textTotalSpent = findViewById(R.id.textTotalSpent);
        recyclerViewCategorySpending = findViewById(R.id.recyclerViewCategorySpending);

        // Initialize RecyclerView and adapter
        recyclerViewCategorySpending.setLayoutManager(new LinearLayoutManager(this));
        List<CategorySpending> categorySpendings = new ArrayList<>();
        categorySpendingAdapter = new CategorySpendingAdapter(categorySpendings);
        recyclerViewCategorySpending.setAdapter(categorySpendingAdapter);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        // Fetch and display
        displayTotalSpent();
        displayCategorySpending();
    }

    @SuppressLint("DefaultLocale")
    private void displayTotalSpent() {
        ExpenseViewModel expenseViewModel = new ViewModelProvider(this).get(ExpenseViewModel.class);
        expenseViewModel.getTotalSpent().observe(this, new Observer<Double>() {
            @Override
            public void onChanged(Double totalSpent) {
                if (totalSpent != null) {
                    textTotalSpent.setText(String.format("Total Spent: $%.2f", totalSpent));
                } else {
                    textTotalSpent.setText("Total Spent: N/A");
                }
            }
        });
    }

    private void displayCategorySpending() {
        ExpenseViewModel expenseViewModel = new ViewModelProvider(this).get(ExpenseViewModel.class);
        expenseViewModel.getCategorySpending().observe(this, new Observer<List<CategorySpending>>() {
            @Override
            public void onChanged(List<CategorySpending> categorySpendings) {
                // Update the RecyclerView with the fetched data
                categorySpendingAdapter.setCategorySpendings(categorySpendings);
            }
        });
    }
}
