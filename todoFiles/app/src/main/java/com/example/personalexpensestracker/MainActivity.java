package com.example.personalexpensestracker;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;

import com.example.personalexpensestracker.Fragments.AddFragment;
import com.example.personalexpensestracker.Fragments.DetailsFragment;
import com.example.personalexpensestracker.Fragments.ListFragment;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Load the fragments into the main activity
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container_add, new AddFragment())
                .replace(R.id.fragment_container_list, new ListFragment())
                .replace(R.id.fragment_container_details, new DetailsFragment())
                .commit();
    }
}
