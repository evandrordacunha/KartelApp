package com.example.kartelapp;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import java.util.HashMap;

public class LoginActivity extends AppCompatActivity {

    private EditText mEditEmail;
    private EditText mEditSenha;
    private Button mButtonLogin;
    private TextView mRecuperaSenha;
    private TextView mCadastro;
    private String emailInformado;
    private String senha;
    private TextView mAdminRedirect;
    HashMap<String, Object> postos = new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        //TELA DE LOGIN MANTEM SEU LAYOUT EM MODO RETRATO
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        //CRIANDO REFERÊNCIAS PARA AS VARIAVEIS E-MAIL E SENHA
        mEditEmail = findViewById(R.id.edit_email);
        mEditSenha = findViewById(R.id.edit_senha);
        mCadastro = findViewById(R.id.v_cadastro);
        mButtonLogin = findViewById(R.id.bt_login);
        mAdminRedirect = findViewById(R.id.V_admin);
        mRecuperaSenha = findViewById(R.id.v_recupera_senha);
        // AÇÃO AO CLICAR EM BOTÃO LOGIN
        mButtonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // PEGANDO O QUE FOI INFORMADO NOS CAMPOS E-MAIL, SENHA
                emailInformado = mEditEmail.getText().toString();
                senha = mEditSenha.getText().toString();

                //GARANTINDO PREENCHIMENTO DOS DADOS OBRIGATÓRIOS E VALIDAÇÕES DE FORMULÁRIO
                if (emailInformado == null || emailInformado.isEmpty()) {
                    Toast.makeText(LoginActivity.this, "Campo EMAIL é obrigatório e deve ser informado!", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (senha == null || senha.isEmpty()) {
                    Toast.makeText(LoginActivity.this, "Campo SENHA é obrigatório e deve ser informado!", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (senha.length() < 6) {
                    Toast.makeText(LoginActivity.this, "Campo SENHA deve ter no mínimo 6 caracteres!", Toast.LENGTH_SHORT).show();
                    return;
                }

                //VALIDANDO SE O E-MAIL INFORMADO É VALIDO
                if (android.util.Patterns.EMAIL_ADDRESS.matcher(emailInformado).matches() != true) {
                    Toast.makeText(LoginActivity.this, "Formato de e-mail inválido!", Toast.LENGTH_SHORT).show();
                    return;
                }

                //VALIDANDO COM FIREBASE A EXISTÊNCIA DO E-MAIL INFORMADO
                FirebaseAuth.getInstance().signInWithEmailAndPassword(emailInformado, senha)
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    Log.i("teste", task.getResult().getUser().getUid());
                                    Intent intent = new Intent(LoginActivity.this, PrincipalActivity.class);
                                    startActivity(intent);
                                }
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.e("teste", e.getMessage());
                                Toast.makeText(LoginActivity.this, "E-mail ou senha inválido!", Toast.LENGTH_SHORT).show();
                            }
                        });
            }
        });

        /**
         * REDIRECIONAMENTO AO CLICAR EM CADASTRAR
         */
        mCadastro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //INTENT MANIPULAR AÇÕES DO BOTÃO, APONTA CLASSE ORIGEM E QUAL CLASSE DESTINO SE ENCONTRA O MÉTODO
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
            }
        });
        /**
         * REDIRECIONAMENTO AO CLICAR EM RECUPERAR SENHA
         */
        mRecuperaSenha.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, RecuperarSenhaActivity.class);
                startActivity(intent);
                // popular();
            }
        });
        /**
         * REDIRECIONA PARA A AREA DE ADMINISTRADOR
         */
        mAdminRedirect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, AdminActivity.class);
                startActivity(intent);
            }
        });
    }
}