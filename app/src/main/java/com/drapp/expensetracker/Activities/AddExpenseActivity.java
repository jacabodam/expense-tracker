package com.drapp.expensetracker.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.drapp.expensetracker.Entities.Expense;
import com.drapp.expensetracker.MyApp;
import com.drapp.expensetracker.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class AddExpenseActivity extends AppCompatActivity {

    private EditText editTextAmount;
    private Button buttonPickDate, buttonAddExpense;
    private Spinner spinnerCategory;
    private TextView labelDate;
    private Calendar calendar;
    private boolean isDatePickerDialogShown = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_expense);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        editTextAmount = findViewById(R.id.editTextAmount);
        buttonPickDate = findViewById(R.id.buttonPickDate);
        spinnerCategory = findViewById(R.id.spinnerCategory);
        buttonAddExpense = findViewById(R.id.buttonAddExpense);
        labelDate = findViewById(R.id.labelDate);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                this, R.array.categories_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerCategory.setAdapter(adapter);

        buttonAddExpense.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addExpense();
            }
        });

        buttonPickDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isDatePickerDialogShown) {
                    showDatePickerDialog();
                    isDatePickerDialogShown = true;
                }
            }
        });

        calendar = Calendar.getInstance();
        updateDateButton();
    }

    private void showDatePickerDialog() {
        DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                calendar.set(Calendar.YEAR, year);
                calendar.set(Calendar.MONTH, monthOfYear);
                calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateDateButton();
                isDatePickerDialogShown = false; // Reset flag
            }
        };

        DatePickerDialog datePickerDialog = new DatePickerDialog(this, dateSetListener,
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH));

        datePickerDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                isDatePickerDialogShown = false; // Reset flag
            }
        });

        datePickerDialog.show(); // Add this line to show the dialog
    }

    private void updateDateButton() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
        buttonPickDate.setText(sdf.format(calendar.getTime()));
    }

    private void addExpense() {
        String category = spinnerCategory.getSelectedItem().toString();
        double amount = Double.parseDouble(editTextAmount.getText().toString());
        String date = buttonPickDate.getText().toString();

        Expense expense = new Expense();
        expense.setCategory(category);
        expense.setAmount(amount);
        expense.setDate(date);

        // Perform database operation asynchronously using a background thread or coroutine
        new Thread(new Runnable() {
            @Override
            public void run() {
                MyApp.getExpenseDatabase().expenseDao().insert(expense);
            }
        }).start();

        Toast.makeText(this, "Expense added successfully", Toast.LENGTH_SHORT).show();
        finish();
    }

}
