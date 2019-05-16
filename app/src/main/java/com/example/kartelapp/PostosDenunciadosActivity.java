package com.example.kartelapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.kartelapp.ui.postosdenunciados.PostosDenunciadosFragment;

public class PostosDenunciadosActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.postos_denunciados_activity);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.container, PostosDenunciadosFragment.newInstance())
                    .commitNow();
        }
    }
}
