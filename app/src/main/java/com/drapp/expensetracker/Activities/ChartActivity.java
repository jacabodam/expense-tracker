package com.drapp.expensetracker.Activities;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.drapp.expensetracker.Entities.Expense;
import com.drapp.expensetracker.Entities.ExpenseViewModel;
import com.drapp.expensetracker.R;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.ValueFormatter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class ChartActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chart);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        toolbar.setNavigationOnClickListener(v -> onBackPressed());

        // Initialize the chart
        BarChart barChart = findViewById(R.id.barChart);

        ExpenseViewModel expenseViewModel = new ViewModelProvider(this).get(ExpenseViewModel.class);
        expenseViewModel.getAllExpenses().observe(this, expenses -> {
            if (expenses != null && !expenses.isEmpty()) {
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

                // Set description for the chart
                Description description = new Description();
                description.setText("Category-wise Spending");
                barChart.setDescription(description);

                // Set x-axis labels (category names)
                XAxis xAxis = barChart.getXAxis();
                xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
                xAxis.setGranularity(1f); // one category per interval
                xAxis.setValueFormatter(new ValueFormatter() {
                    @Override
                    public String getFormattedValue(float value) {
                        // Convert float value to index for accessing category names
                        int index = (int) value;
                        if (index >= 0 && index < entries.size()) {
                            // Get category name from the entry at the corresponding index
                            return entries.get(index).getData().toString();
                        }
                        return "";
                    }
                });

                // Invalidate the chart to refresh its display
                barChart.invalidate();
            } else {
                Toast.makeText(this, "No expenses to display", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private List<BarEntry> processExpenseData(List<Expense> expenses) {
        // Initialize a map to store category-wise total spending
        Map<String, Float> categoryTotalMap = new HashMap<>();

        // Iterate through expenses to aggregate spending by category
        for (Expense expense : expenses) {
            String category = expense.getCategory();
            float amount = (float) expense.getAmount();

            // Update total spending for the category
            if (categoryTotalMap.containsKey(category)) {
                float total = categoryTotalMap.get(category);
                categoryTotalMap.put(category, total + amount);
            } else {
                categoryTotalMap.put(category, amount);
            }
        }

        // Convert the aggregated data into BarEntry objects
        List<BarEntry> entries = new ArrayList<>();
        int index = 0;
        for (Map.Entry<String, Float> entry : categoryTotalMap.entrySet()) {
            String category = entry.getKey();
            float totalSpending = entry.getValue();
            entries.add(new BarEntry(index, totalSpending, category));
            index++;
        }


        return entries;
    }


}
