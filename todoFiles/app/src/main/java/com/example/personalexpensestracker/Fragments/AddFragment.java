package com.example.personalexpensestracker.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.personalexpensestracker.DataBaseHelper;
import com.example.personalexpensestracker.Expenses;
import com.example.personalexpensestracker.R;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class AddFragment extends Fragment {

    private EditText expenseAmount;
    private EditText expenseNotes;
    private Spinner expenseTypeSpinner;
    private Button addExpenseButton;
    private DataBaseHelper dbHelper;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add, container, false);

        expenseTypeSpinner = view.findViewById(R.id.expenseTypeSpinner);
        expenseAmount = view.findViewById(R.id.expenseAmount);
        expenseNotes = view.findViewById(R.id.expenseNotes);
        addExpenseButton = view.findViewById(R.id.addExpenseButton);

        // Populate the Spinner with predefined expense types
        String[] options = {"Food", "Transport", "Entertainment", "Others"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_item, options);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        expenseTypeSpinner.setAdapter(adapter);

        // Initialize DataBaseHelper
        dbHelper = new DataBaseHelper(getActivity(), "expenses", null, 1);

        addExpenseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addExpense();
            }
        });

        return view;
    }

    private void addExpense() {
        String type = expenseTypeSpinner.getSelectedItem() != null ? expenseTypeSpinner.getSelectedItem().toString() : "";
        String amountStr = expenseAmount.getText().toString();
        String notes = expenseNotes.getText().toString();

        if (type.isEmpty() || amountStr.isEmpty()) {
            Toast.makeText(getActivity(), "Please fill all required fields", Toast.LENGTH_SHORT).show();
            return;
        }

        try {
            double amount = Double.parseDouble(amountStr);
            // Create an Expenses object
            Expenses expense = new Expenses(type, amount, notes);


            // Insert into the database
            dbHelper.addExpense(expense);
            Toast.makeText(getActivity(), "Expense added successfully", Toast.LENGTH_SHORT).show();

            // Clear input fields
            expenseAmount.setText("");
            expenseNotes.setText("");
            expenseTypeSpinner.setSelection(0);

        } catch (NumberFormatException e) {
            Toast.makeText(getActivity(), "Invalid amount format", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            Toast.makeText(getActivity(), "Error adding expense: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }
}
