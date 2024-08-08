package com.example.personalexpensestracker.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.personalexpensestracker.DataBaseHelper;
import com.example.personalexpensestracker.Expenses;
import com.example.personalexpensestracker.R;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class ListFragment extends Fragment {

    private DataBaseHelper dbHelper;
    private List<Expenses> expenses;
    private ArrayAdapter<Expenses> adapter;
    private final DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm:ss");
    private SelectItemInList selectItemInList;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_list, container, false);

        dbHelper = new DataBaseHelper(getActivity(), "expenses", null, 1);
        ListView expenseListView = view.findViewById(R.id.expenseListView);

        // Initialize expenses list
        expenses = new ArrayList<>();

        adapter = new ArrayAdapter<Expenses>(
                getActivity(), android.R.layout.simple_list_item_1, expenses) {
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                if (convertView == null) {
                    convertView = LayoutInflater.from(getContext()).inflate(android.R.layout.simple_list_item_1, parent, false);
                }
                Expenses expense = getItem(position);

                if (expense != null) {
                    String displayText = expense.getType() + " - " + expense.getTime().format(timeFormatter);
                    TextView textView = convertView.findViewById(android.R.id.text1);
                    textView.setText(displayText);

                    // Apply font size
                    float fontSize = getFontSize();
                    textView.setTextSize(fontSize);
                }

                return convertView;
            }
        };

        expenseListView.setAdapter(adapter);
        expenseListView.setOnItemClickListener((parent, view1, position, id) -> {
            Expenses selectedExpense = adapter.getItem(position);
            if (selectedExpense != null) {
                int expenseId = selectedExpense.getId();

                if (selectItemInList != null) {
                    selectItemInList.onExpenseSelected(expenseId);
                }
            }
        });

        // Initial load of expenses
        refreshExpenses();

        return view;
    }

    public void refreshExpenses() {
        expenses.clear();
        expenses.addAll(dbHelper.getAllExpenses());
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        try {
            selectItemInList = (SelectItemInList) requireActivity();
        } catch (ClassCastException e) {
            throw new ClassCastException(requireActivity().toString() + " must implement OnExpenseSelectedListener");
        }
    }

    public void changeFontSize() {
        // Refresh the font size for the list items
        adapter.notifyDataSetChanged();
    }

    private float getFontSize() {
        // Retrieve font size from shared preferences
        SharedPrefManager sharedPrefManager = SharedPrefManager.getInstance(getActivity());
        String fontSizeString = sharedPrefManager.readString("fontSize", "18");
        return Float.parseFloat(fontSizeString);
    }

    public interface SelectItemInList {
        void onExpenseSelected(int expenseId);
    }
}
