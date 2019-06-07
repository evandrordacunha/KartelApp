package com.example.kartelapp;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;

import com.example.kartelapp.ui.postosconfiaveis.PostosConfiaveisFragment;

public class PostosConfiaveisActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.postos_confiaveis_activity);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.container, PostosConfiaveisFragment.newInstance())
                    .commitNow();
        }
    }
}
