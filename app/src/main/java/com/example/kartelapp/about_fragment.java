package com.example.kartelapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.example.kartelapp.ui.about.AboutFragmentFragment;

public class about_fragment extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.about_fragment_activity);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.container, AboutFragmentFragment.newInstance())
                    .commitNow();
        }
    }
}
