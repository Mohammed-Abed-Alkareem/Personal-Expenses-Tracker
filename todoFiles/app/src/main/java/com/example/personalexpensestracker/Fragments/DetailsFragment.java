package com.example.personalexpensestracker.Fragments;

import android.content.Context;
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
import com.example.personalexpensestracker.R;

public class DetailsFragment extends Fragment {
    private TextView expenseDetails;
    private ImageButton deleteButton;
    private ImageButton plusButton;
    private ImageButton minusButton;
    private TextView fontSizeTextView;
    private DataBaseHelper dbHelper;
    private SharedPreferences sharedPreferences;

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
        sharedPreferences = getActivity().getSharedPreferences("ExpenseTrackerPrefs", Context.MODE_PRIVATE);

        // Set initial font size
        int fontSize = sharedPreferences.getInt("fontSize", 18); // Default to 18sp
        expenseDetails.setTextSize(fontSize);
        fontSizeTextView.setText("Font Size: " + fontSize + "sp");

        deleteButton.setOnClickListener(v -> {
            // Handle delete expense
            // ...
        });

        plusButton.setOnClickListener(v -> {
            // Increase font size
            float currentSize = expenseDetails.getTextSize() / getResources().getDisplayMetrics().scaledDensity;
            if (currentSize < 48) { // Maximum font size limit
                currentSize += 2;
                expenseDetails.setTextSize(currentSize);
                fontSizeTextView.setText("Font Size: " + (int)currentSize + "sp");
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putInt("fontSize", (int)currentSize);
                editor.apply();
            }
        });

        minusButton.setOnClickListener(v -> {
            // Decrease font size
            float currentSize = expenseDetails.getTextSize() / getResources().getDisplayMetrics().scaledDensity;
            if (currentSize > 10) { // Minimum font size limit
                currentSize -= 2;
                expenseDetails.setTextSize(currentSize);
                fontSizeTextView.setText("Font Size: " + (int)currentSize + "sp");
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putInt("fontSize", (int)currentSize);
                editor.apply();
            }
        });

        return view;
    }

    public void displayExpenseDetails(String details) {
        expenseDetails.setText(details);
    }
}
