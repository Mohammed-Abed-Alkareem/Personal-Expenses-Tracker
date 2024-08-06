package com.example.personalexpensestracker.Fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.personalexpensestracker.DataBaseHelper;
import com.example.personalexpensestracker.R;


import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import java.util.ArrayList;

public class ListFragment extends Fragment {
    private ListView expenseListView;
    private ArrayAdapter<String> adapter;
    private ArrayList<String> expenses;
    private DataBaseHelper dbHelper;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_list, container, false);

        expenseListView = view.findViewById(R.id.expenseListView);
        dbHelper = new DataBaseHelper(getActivity(), "expenses", null, 1);
        expenses = new ArrayList<>();

        loadExpenses();

        adapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_1, expenses);
        expenseListView.setAdapter(adapter);

        expenseListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // Handle item click to show details in DetailsFragment
                String selectedExpense = expenses.get(position);
                DetailsFragment detailsFragment = (DetailsFragment) getFragmentManager().findFragmentById(R.id.fragment_container_details);
                if (detailsFragment != null) {
                    detailsFragment.displayExpenseDetails(selectedExpense);
                }
            }
        });

        return view;
    }

    private void loadExpenses() {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.query(DataBaseHelper.TABLE_EXPENSES, null, null, null, null, null, null);

        if (cursor != null) {
            while (cursor.moveToNext()) {
                String type = cursor.getString(cursor.getColumnIndex(DataBaseHelper.COLUMN_TYPE));
                String time = cursor.getString(cursor.getColumnIndex(DataBaseHelper.COLUMN_TIME));
                expenses.add(type + " at " + time);
            }
            cursor.close();
        }
    }

    public void refreshExpenses() {
        expenses.clear();
        loadExpenses();
        adapter.notifyDataSetChanged();
    }
}
