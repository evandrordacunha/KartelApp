package com.example.kartelapp;

import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.List;

public class IndexActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_index);
        verificaAutenticacao();

    }

    /**
     * VERIFICA SE O USUÁRIO É CREDENCIADO OU NÃO, NÃO PERMITE QUE ACESSE A TELA PRINCIPAL CASO NAO ESTEJA
     */
    private void verificaAutenticacao() {
        //SE USUÁRIO NÃO ESTÁ LOGADO
        if (FirebaseAuth.getInstance().getUid() == null) {
            Intent intent = new Intent(IndexActivity.this, LoginActivity.class);
            //SOBREPONDO ACTIVITY DE LOGIN, NÃO EXIBINDO A INDEX
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        }
    }
}
