package com.example.kartelapp;

import android.content.Intent;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;

import com.google.firebase.auth.FirebaseAuth;

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
