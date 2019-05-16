package com.example.kartelapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.kartelapp.ui.compartilhar.CompartilharFragment;

public class CompartilharActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.compartilhar_activity);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.container, CompartilharFragment.newInstance())
                    .commitNow();
        }
    }
}
