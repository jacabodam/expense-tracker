package com.drapp.expensetracker.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.Spinner;

import com.drapp.expensetracker.Adapters.ExpenseAdapter;
import com.drapp.expensetracker.Entities.Expense;
import com.drapp.expensetracker.Entities.ExpenseViewModel;
import com.drapp.expensetracker.R;
import com.github.mikephil.charting.charts.Chart;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.lifecycle.ViewModelProvider;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Random;


public class ViewExpensesActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private ExpenseAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_expenses);

        // Initialize RecyclerView
        recyclerView = findViewById(R.id.recyclerViewExpenses);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Initialize adapter
        adapter = new ExpenseAdapter();
        recyclerView.setAdapter(adapter);

        // Fetch expenses from Room database
        observeExpenses();
        generateSampleDataIfRequired();

        FloatingActionButton fabAddExpense = findViewById(R.id.fabAddExpense);
        fabAddExpense.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ViewExpensesActivity.this, AddExpenseActivity.class);
                startActivity(intent);
            }
        });

        FloatingActionButton fabViewChart = findViewById(R.id.fabViewChart);
        fabViewChart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ViewExpensesActivity.this, ChartActivity.class);
                startActivity(intent);
            }
        });

        FloatingActionButton fabViewReport = findViewById(R.id.fabTotalSpending);

        // Set click listener for floating action button
        fabViewReport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Navigate to the report activity
                Intent intent = new Intent(ViewExpensesActivity.this, ReportActivity.class);
                startActivity(intent);
            }
        });

        Spinner spinnerCategoryFilter = findViewById(R.id.spinnerCategoryFilter);
        Button buttonPickDate = findViewById(R.id.buttonPickDate);
        Button buttonApplyFilter = findViewById(R.id.buttonApplyFilter);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                this, R.array.categories_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerCategoryFilter.setAdapter(adapter);

        buttonPickDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final Calendar calendar = Calendar.getInstance();
                int year = calendar.get(Calendar.YEAR);
                int month = calendar.get(Calendar.MONTH);
                int day = calendar.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog datePickerDialog = new DatePickerDialog(ViewExpensesActivity.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                                String selectedDate = year + "-" + (monthOfYear + 1) + "-" + dayOfMonth;
                                buttonPickDate.setText(selectedDate);
                            }
                        }, year, month, day);

                datePickerDialog.show();
            }
        });

        buttonApplyFilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String selectedCategory = spinnerCategoryFilter.getSelectedItem().toString();
                String date = buttonPickDate.getText().toString();

                try {
                    // Parse the date string into a Date object
                    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
                    Date parsedDate = dateFormat.parse(date);

                    // Format the parsed date back into the desired format
                    SimpleDateFormat filterDateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
                    String formattedStartDate = filterDateFormat.format(parsedDate);

                    filterExpenses(selectedCategory, formattedStartDate);
                } catch (ParseException e) {
                    e.printStackTrace();
                    // Handle the parse exception, if any
                }
            }
        });

        Button buttonClearFilter = findViewById(R.id.buttonClearFilter);

        buttonClearFilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                spinnerCategoryFilter.setSelection(0);

                buttonPickDate.setText("");

                observeExpenses();
            }
        });


    }

    private void filterExpenses(String category, String startDate) {

        ExpenseViewModel expenseViewModel = new ViewModelProvider(this).get(ExpenseViewModel.class);

        expenseViewModel.getFilteredExpenses(category, startDate).observe(this, new Observer<List<Expense>>() {
            @Override
            public void onChanged(List<Expense> filteredExpenses) {
                adapter.setExpenses(filteredExpenses);
            }
        });
    }


    private void observeExpenses() {

        ExpenseViewModel expenseViewModel = new ViewModelProvider(this).get(ExpenseViewModel.class);

        expenseViewModel.getAllExpenses().observe(this, new Observer<List<Expense>>() {
            @Override
            public void onChanged(List<Expense> expenses) {
                //  fetched expenses
                adapter.setExpenses(expenses);
            }
        });
    }

    private void generateSampleDataIfRequired() {
        ExpenseViewModel expenseViewModel = new ViewModelProvider(this).get(ExpenseViewModel.class);
        expenseViewModel.getAllExpenses().observe(this, new Observer<List<Expense>>() {
            @Override
            public void onChanged(List<Expense> expenses) {
                if (expenses == null || expenses.isEmpty()) {
                    // Generate sample data if there is no existing data
                    List<Expense> sampleExpenses = generateSampleExpenses();
                    expenseViewModel.insertAll(sampleExpenses);
                }
            }
        });
    }

    private List<Expense> generateSampleExpenses() {
        List<Expense> sampleExpenses = new ArrayList<>();
        String[] categories = {"Food", "Transport", "Entertainment", "Shopping", "Utilities"};
        Random random = new Random();

        for (int i = 0; i < 5; i++) {
            String category = categories[random.nextInt(categories.length)];
            double amount =  random.nextInt(101); // Generate random amount between 0 and 100

            Expense expense = new Expense();
            expense.setCategory(category);
            expense.setAmount(amount);

            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.US);

            String date = sdf.format( Calendar.getInstance().getTime());

            expense.setDate(date);

            sampleExpenses.add(expense);
        }
        return sampleExpenses;
    }
}

