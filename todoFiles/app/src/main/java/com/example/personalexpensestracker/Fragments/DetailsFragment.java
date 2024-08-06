package com.example.personalexpensestracker.Fragments;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.personalexpensestracker.DataBaseHelper;
import com.example.personalexpensestracker.Expenses;
import com.example.personalexpensestracker.R;
import com.example.personalexpensestracker.Fragments.SharedPrefManager;

public class DetailsFragment extends Fragment {
    private TextView expenseDetails;
    private ImageButton deleteButton;
    private ImageButton plusButton;
    private ImageButton minusButton;
    private TextView fontSizeTextView;
    private DataBaseHelper dbHelper;
    private SharedPrefManager sharedPrefManager;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_details, container, false);

        expenseDetails = view.findViewById(R.id.expenseDetails);
        deleteButton = view.findViewById(R.id.deleteButton);
        plusButton = view.findViewById(R.id.plusButton);
        minusButton = view.findViewById(R.id.minusButton);
        fontSizeTextView = view.findViewById(R.id.fontSize);

        dbHelper = new DataBaseHelper(getActivity(), "expenses", null, 1);
        sharedPrefManager = SharedPrefManager.getInstance(getActivity());
        String fontSizeString = sharedPrefManager.readString("fontSize", "18");

        expenseDetails.setTextSize(Float.parseFloat(fontSizeString));
        fontSizeTextView.setText("Font Size: " + fontSizeString + "sp");

        deleteButton.setOnClickListener(v -> {
            // Delete the expense
            String[] details = expenseDetails.getText().toString().split("\n");
            String[] idString = details[0].split(": ");
            int expenseId = Integer.parseInt(idString[1]);
            dbHelper.deleteExpense(expenseId);
            expenseDetails.setText("");
        });

        plusButton.setOnClickListener(v -> {
            // Increase font size
            float currentSize = expenseDetails.getTextSize() / getResources().getDisplayMetrics().scaledDensity;
            if (currentSize < 48) { // Maximum font size limit
                currentSize += 2;
                expenseDetails.setTextSize(currentSize);
                fontSizeTextView.setText("Font Size: " + (int)currentSize + "sp");
                sharedPrefManager.writeString("fontSize", String.valueOf((int)currentSize));
            }
        });

        minusButton.setOnClickListener(v -> {
            // Decrease font size
            float currentSize = expenseDetails.getTextSize() / getResources().getDisplayMetrics().scaledDensity;
            if (currentSize > 10) { // Minimum font size limit
                currentSize -= 2;
                expenseDetails.setTextSize(currentSize);
                fontSizeTextView.setText("Font Size: " + (int)currentSize + "sp");
                sharedPrefManager.writeString("fontSize", String.valueOf((int)currentSize));
            }
        });

        return view;
    }

    public void displayExpenseDetails(String details) {
        expenseDetails.setText(details);
    }

    public void updateExpense(int expenseId) {
        Expenses expense = dbHelper.getExpenseById(expenseId);
        if (expense != null) {
            String details = "Type: " + expense.getType() + "\n" +
                    "Amount: " + expense.getAmount() + "\n" +
                    "Notes: " + expense.getNotes() + "\n" +
                    "Time: " + expense.getTime().toString(); // Update this as needed
            displayExpenseDetails(details);
        }
    }
}
