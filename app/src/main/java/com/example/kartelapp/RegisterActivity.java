package com.example.kartelapp;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;
import java.util.UUID;

public class RegisterActivity extends AppCompatActivity {


    private EditText mEditEmail;
    private EditText mEditSenha;
    private EditText mEditNome;
    private Button mButtonCadastrar;
    private Button mButtonImg;
    private Uri mSelectUri;
    //REFERÊNCIA PARA A IMAGEVIEW DA FOTO FO PERFIL
    private ImageView mFotoPerfil;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity__register);

        //CRIANDO REFERÊNCIAS
        mEditEmail = findViewById(R.id.edit_email_cad);
        mEditSenha = findViewById(R.id.edit_senha_cad);
        mEditNome = findViewById(R.id.edit_nome);
        mButtonImg = findViewById(R.id.bt_foto);
        mFotoPerfil = findViewById(R.id.img_foto_user);
        mButtonImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selecionarFoto();
            }
        });
        mButtonCadastrar = findViewById(R.id.bt_cadastrar);
        mButtonCadastrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createUser();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 0) {
            mSelectUri = data.getData();
            //BITMAP VAI RECEBER A IMAGEM
            Bitmap bitmap = null;
            try {

                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), mSelectUri);
                mFotoPerfil.setImageDrawable(new BitmapDrawable(bitmap));
                mButtonImg.setAlpha(0);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * AUTENTICANDO USUARIO COM O FIREBASE AUTHENTICATION
     */
    private void createUser() {
        //PEGANDO DADOS INFORMADOS NO FORMULARIO
        String nomeInformado = mEditNome.getText().toString();
        String emailInformado = mEditEmail.getText().toString();
        String senhaInformada = mEditSenha.getText().toString();

        //GARANTINDO PREENCHIMENTO DOS DADOS OBRIGATÓRIOS E VALIDAÇÕES DE FORMULÁRIO
        if (emailInformado == null || emailInformado.isEmpty()) {
            Toast.makeText(this, "Campo EMAIL é obrigatório e deve ser informado!", Toast.LENGTH_SHORT).show();
            return;
        }
        if (nomeInformado == null || nomeInformado.isEmpty()) {
            Toast.makeText(this, "Campo NOME é obrigatório e deve ser informado!", Toast.LENGTH_SHORT).show();
            return;
        }
        if (senhaInformada == null || senhaInformada.isEmpty()) {
            Toast.makeText(this, "Campo SENHA é obrigatório e deve ser informado!", Toast.LENGTH_SHORT).show();
            return;
        }
        if (senhaInformada.length() < 6) {
            Toast.makeText(this, "Campo SENHA deve ter no mínimo 6 caracteres!", Toast.LENGTH_SHORT).show();
            return;
        }

        //VALIDANDO SE O E-MAIL INFORMADO É VALIDO
        if (android.util.Patterns.EMAIL_ADDRESS.matcher(emailInformado).matches() != true) {
            Toast.makeText(this, "Formato de e-mail inválido!", Toast.LENGTH_SHORT).show();
            return;
        }

        //GRAVANDO DADOS DE USUÁRIO NO FIREBASE AUTHENTICATION
        FirebaseAuth.getInstance().createUserWithEmailAndPassword(emailInformado, senhaInformada)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Log.i("DADOS INSERIDOS!", task.getResult().getUser().getUid());
                            Toast.makeText(RegisterActivity.this, "Cadastro realizado com sucesso!", Toast.LENGTH_SHORT).show();
                            //SE O CADASTRO FOR REALIZADO COM SUCESSO, VOLTA PARA A TELA DE LOGIN APÓS INSERÇÃO DE DADOS
                            Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                            startActivity(intent);
                            saveUserInFirebase();
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.i("ERRO ENCONTRADO!", e.getMessage());
                    }
                });
    }

    /**
     * MÉTODO RESPONSÁVEL POR SELECIONAR A FOTO DO PERFIL DE USUÁRIO
     */
    private void selecionarFoto() {
        //ABRINDO A GALERIA DE FOTOS
        Intent intent = new Intent(Intent.ACTION_PICK);
        //TIPO DE DADOS QUE VOU PERMITIR A INSERÇÃO
        intent.setType("image/*");
        startActivityForResult(intent, 0);
    }

    /***
     * MÉTODO RESPONSÁVEL POR PEGAR OS DADOS DOS USUÁRIOS E SALVAR NA BASE DE DADOS DO FIREBASE APÓS AUTENTICAÇÃO
     */
    private void saveUserInFirebase() {

        //CRIANDO A HASH PARA REFERÊNCIA DO NOME DO ARQUIVO
        String fileName = UUID.randomUUID().toString();
        final StorageReference ref = FirebaseStorage.getInstance().getReference("/images/" + fileName);
        //SUBINDO A FOTO PARA A PASTA IMAGES DO FIREBASE
        ref.putFile(mSelectUri)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        //PEGA A REFERÊNCIA DA URL ONDE ESTÁ HOSPEDADO NO FIREBASE A IMAGEM DEPOIS DE ELA TER SUBIDO PARA QUE EU POSSA TRABALHAR COM ELA
                        ref.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                Log.i("URI = ", uri.toString());
                                //PEGANDO DADOS DO BANCO DE DADOS [ id, nome de usuário, e-mail e senha]
                                final String userID = mEditEmail.getText().toString();
                                String userName = mEditNome.getText().toString();
                                String profileUrl = uri.toString();
                                //USUÁRIO NOVO CADASTRADO
                                User usuario = new User(userID, userName, profileUrl);

                                //CRIANDO A REFERÂNCIA PARA UMA COLEÇÃO DE USUÁRIOS
                                FirebaseFirestore.getInstance().collection("usuarios").document(userID)
                                        .set(usuario)
                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void aVoid) {
                                                Log.d("Usuario inserido ", userID);
                                            }
                                        }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Log.e("Erro ao inserir ", userID);
                                    }
                                });
                            }
                        });
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.e("###ERRO###", e.getMessage(), e);
                    }
                });
    }
}