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
    int expense_id;
    private TextView expenseDetails;
    private ImageButton deleteButton;
    private ImageButton plusButton;
    private ImageButton minusButton;
    private TextView fontSizeTextView;
    private DataBaseHelper dbHelper;
    private SharedPrefManager sharedPrefManager;
    private OnExpenseDeletedListener listener;

    private OnExpenseDeletedListener listener_fontSize;

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
            dbHelper.deleteExpense(expense_id);
            expenseDetails.setText("");
            //notify the listener
            // Notify the listener
            if (listener != null) {
                listener.onExpenseDeleted();
            }

            if (listener_fontSize != null) {
                listener_fontSize.onFontSizeChanged();
            }

        });

        plusButton.setOnClickListener(v -> {
            // Increase font size
            float currentSize = expenseDetails.getTextSize() / getResources().getDisplayMetrics().scaledDensity;
            if (currentSize < 30) { // Maximum font size limit
                currentSize += 2;
                expenseDetails.setTextSize(currentSize);
                fontSizeTextView.setText("Font Size: " + (int) currentSize + "sp");
                sharedPrefManager.writeString("fontSize", String.valueOf((int) currentSize));
                listener_fontSize.onFontSizeChanged();
            }
        });

        minusButton.setOnClickListener(v -> {
            // Decrease font size
            float currentSize = expenseDetails.getTextSize() / getResources().getDisplayMetrics().scaledDensity;
            if (currentSize > 10) { // Minimum font size limit
                currentSize -= 2;
                expenseDetails.setTextSize(currentSize);
                fontSizeTextView.setText("Font Size: " + (int) currentSize + "sp");
                sharedPrefManager.writeString("fontSize", String.valueOf((int) currentSize));

                listener_fontSize.onFontSizeChanged();
            }
        });

        return view;
    }

    public void displayExpenseDetails(String details) {
        expenseDetails.setText(details);
    }

    public void updateExpense(int expenseId) {
        expense_id = expenseId;
        Expenses expense = dbHelper.getExpenseById(expenseId);
        if (expense != null) {
            // store each expense detail in a string
            String type = expense.getType();
            double amount = expense.getAmount();
            String notes = expense.getNotes();
            String date = expense.getDate().toString();
            String time = expense.getTime().toString();
            if (notes.isEmpty()) {
                notes = "No notes";
            }
            String details = "Type: " + type + "\nAmount: " + amount + "\nNotes: " + notes + "\nDate: " + date + "\nTime: " + time;
            displayExpenseDetails(details);
        }
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        try {
            listener = (OnExpenseDeletedListener) requireActivity();
            listener_fontSize = (OnExpenseDeletedListener) requireActivity();
        } catch (ClassCastException e) {
            throw new ClassCastException(requireActivity().toString() + " must implement OnExpenseDeletedListener");
        }
    }

    public interface OnExpenseDeletedListener {
        void onExpenseDeleted();

        void onFontSizeChanged();

    }
}

