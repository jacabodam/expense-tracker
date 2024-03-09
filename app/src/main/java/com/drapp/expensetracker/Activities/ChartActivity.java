package com.drapp.expensetracker.Activities;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.drapp.expensetracker.Entities.Expense;
import com.drapp.expensetracker.Entities.ExpenseViewModel;
import com.drapp.expensetracker.R;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;

import java.util.ArrayList;
import java.util.List;

public class ChartActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chart);

        // Initialize the chart
        BarChart barChart = findViewById(R.id.barChart);

        ExpenseViewModel expenseViewModel = new ViewModelProvider(this).get(ExpenseViewModel.class);
        expenseViewModel.getAllExpenses().observe(this, new Observer<List<Expense>>() {
            @Override
            public void onChanged(List<Expense> expenses) {
                // Process the expense data to get values for the chart
                List<BarEntry> entries = processExpenseData(expenses);

                // Create a BarDataSet with the processed data
                BarDataSet dataSet = new BarDataSet(entries, "Expenses");

                // Customize the appearance of the data set if needed
                dataSet.setColor(Color.rgb(0, 155, 0));

                // Create a BarData object with the BarDataSet
                BarData barData = new BarData(dataSet);
                barData.setBarWidth(0.9f);

                // Set the data to the chart
                barChart.setData(barData);

                // Invalidate the chart to refresh its display
                barChart.invalidate();
            }
        });
    }
     private List<BarEntry> processExpenseData(List<Expense> expenses) {
        List<BarEntry> entries = new ArrayList<>();

        for (int i = 0; i < expenses.size(); i++) {
            float amount = (float) expenses.get(i).getAmount();
            entries.add(new BarEntry(i, amount));
        }
        return entries;
    }

}
