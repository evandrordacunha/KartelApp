package com.example.kartelapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.kartelapp.ui.parceirosindicados.ParceirosIndicadosFragment;

public class ParceirosIndicadosActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.parceiros_indicados_activity);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.container, ParceirosIndicadosFragment.newInstance())
                    .commitNow();
        }
    }
}
