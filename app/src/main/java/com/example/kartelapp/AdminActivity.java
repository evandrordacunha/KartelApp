package com.example.kartelapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreSettings;

public class AdminActivity extends AppCompatActivity {

    private Button atualizaDistribuidores;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);

        atualizaDistribuidores = findViewById(R.id.bt_atualiza_distribuidores);
        atualizaDistribuidores.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //CRIANDO REFERENCIA PARA O FIRESTORE
                FirebaseFirestore db = FirebaseFirestore.getInstance();
                FirebaseFirestoreSettings settings = new FirebaseFirestoreSettings.Builder()
                        .setPersistenceEnabled(true)
                        .build();
                db.setFirestoreSettings(settings);

                Dados dado = new Dados(db);
                dado.inserirPostos(AdminActivity.this);

            }
        });
    }
}
