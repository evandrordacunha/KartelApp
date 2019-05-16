package com.example.kartelapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.kartelapp.ui.sobre.SobreFragment;

public class SobreActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sobre_activity);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.container, SobreFragment.newInstance())
                    .commitNow();
        }
    }
}
