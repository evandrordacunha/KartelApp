package com.example.kartelapp;

import android.content.pm.ActivityInfo;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class RecuperarSenhaActivity extends AppCompatActivity {

    private EditText mEditEmailRecupera;
    private Button mButtonRecuperarSenha;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recupera_senha);
        //TELA DE RECUPERAÇÃO DE SENHA SE MANTEM NO LAYOUT EM MODO RETRATO
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        //BUSCANDO REFERENCIA PARA AS VARIÁVEIS DECLARADAS ACIMA
        mEditEmailRecupera = findViewById(R.id.edit_email_recupera);
        mButtonRecuperarSenha = findViewById(R.id.bt_recuperarSenha);
        // PEGANDO O QUE FOI INFORMADO NOS CAMPOS E-MAIL, SENHA

        mButtonRecuperarSenha.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                //SINCRONIZANDO AUTENTICADOR DO FIREBASE
                FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
                String emailInformado = mEditEmailRecupera.getText().toString();
                //GARANTINDO PREENCHIMENTO DOS DADOS OBRIGATÓRIOS E VALIDAÇÕES DE FORMULÁRIO
                if (emailInformado == null || emailInformado.isEmpty()) {
                    Toast.makeText(RecuperarSenhaActivity.this, "Campo EMAIL é obrigatório e deve ser informado!", Toast.LENGTH_SHORT).show();
                    return;
                }
                //VALIDANDO SE O E-MAIL INFORMADO É VALIDO
                if (android.util.Patterns.EMAIL_ADDRESS.matcher(emailInformado).matches() != true) {
                    Toast.makeText(RecuperarSenhaActivity.this, "Formato de e-mail inválido!", Toast.LENGTH_SHORT).show();
                    return;
                }
                firebaseAuth.sendPasswordResetEmail(emailInformado).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(RecuperarSenhaActivity.this, "Foi enviado um link de redefinição de senha para seu e-mail!", Toast.LENGTH_LONG).show();
                        } else {
                            Toast.makeText(RecuperarSenhaActivity.this, "E-mail não cadastrado!", Toast.LENGTH_LONG).show();
                        }
                    }
                });
            }
        });
    }
}
