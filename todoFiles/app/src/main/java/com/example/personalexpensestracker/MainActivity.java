package com.example.personalexpensestracker;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import com.example.personalexpensestracker.Fragments.AddFragment;
import com.example.personalexpensestracker.Fragments.DetailsFragment;
import com.example.personalexpensestracker.Fragments.ListFragment;

public class MainActivity extends AppCompatActivity implements AddFragment.AddedItem, ListFragment.SelectItemInList, DetailsFragment.ChangeInDetailedView {

    DataBaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container_add, new AddFragment())
                    .replace(R.id.fragment_container_list, new ListFragment())
                    .replace(R.id.fragment_container_details, new DetailsFragment())
                    .commit();
        }
    }

    @Override
    public void onExpenseAdded() {
        ListFragment listFragment = (ListFragment) getSupportFragmentManager().findFragmentById(R.id.fragment_container_list);
        if (listFragment != null) {
            listFragment.refreshExpenses();
        }
    }

    @Override
    public void onExpenseSelected(int expenseId) {
        DetailsFragment detailsFragment = (DetailsFragment) getSupportFragmentManager().findFragmentById(R.id.fragment_container_details);
        if (detailsFragment != null) {
            detailsFragment.updateExpense(expenseId);
        }
    }

    @Override
    public void onExpenseDeleted() {
        ListFragment listFragment = (ListFragment) getSupportFragmentManager().findFragmentById(R.id.fragment_container_list);
        if (listFragment != null) {
            listFragment.refreshExpenses();
        }


    }
    @Override
    public void onFontSizeChanged(){
        // change font size in the other fragments
        AddFragment addFragment = (AddFragment) getSupportFragmentManager().findFragmentById(R.id.fragment_container_add);
        if (addFragment != null) {
            addFragment.changeFontSize();
        }

        ListFragment listFragment = (ListFragment) getSupportFragmentManager().findFragmentById(R.id.fragment_container_list);
        if (listFragment != null) {
            listFragment.changeFontSize();
        }


    }

}
