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

public class AddFragment extends Fragment {

    private EditText expenseAmount;
    private EditText expenseNotes;
    private Spinner expenseTypeSpinner;
    private Button addExpenseButton;
    private DataBaseHelper databasehelper;
    private AddedItem listenerForAdding;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add, container, false);

        expenseTypeSpinner = view.findViewById(R.id.expenseTypeSpinner);
        expenseAmount = view.findViewById(R.id.expenseAmount);
        expenseNotes = view.findViewById(R.id.expenseNotes);
        addExpenseButton = view.findViewById(R.id.addExpenseButton);


        // Initialize DataBaseHelper
        databasehelper = new DataBaseHelper(getActivity(), "expenses", null, 1);

        // Populate the Spinner with predefined expense types
        String[] options = databasehelper.get_types();
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_item, options);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        expenseTypeSpinner.setAdapter(adapter);

        addExpenseButton.setOnClickListener(v ->
                addExpense()
        );


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
            Expenses expense = new Expenses(0,type, amount, notes);
            databasehelper.addExpense(expense);
            Toast.makeText(getActivity(), "Expense added successfully", Toast.LENGTH_SHORT).show();

            // Notify the listener
            if (listenerForAdding != null) {
                listenerForAdding.onExpenseAdded();
            }

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

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        try {
            listenerForAdding = (AddedItem) requireActivity();
        } catch (ClassCastException e) {
            throw new ClassCastException(requireActivity().toString() + " must implement OnExpenseAddedListener");
        }
    }

    public void changeFontSize() {
        //get font size from shared preferences
        SharedPrefManager sharedPrefManager = SharedPrefManager.getInstance(getActivity());
        String fontSizeString = sharedPrefManager.readString("fontSize", "18");

        //convert font size to float
        float fontSize = Float.parseFloat(fontSizeString);

        //set font size for the views
        expenseAmount.setTextSize(fontSize);
        expenseNotes.setTextSize(fontSize);
        addExpenseButton.setTextSize(fontSize);

    }

    public interface AddedItem {
        void onExpenseAdded();
    }
}
