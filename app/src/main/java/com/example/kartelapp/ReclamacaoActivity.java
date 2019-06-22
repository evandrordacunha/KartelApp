package com.example.kartelapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
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
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreSettings;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

public class ReclamacaoActivity extends AppCompatActivity {

    private EditText mEditDataAtendimento;
    private EditText mEditCnpjPosto;
    private EditText mEditEmail;
    private EditText mEditMotivo;
    private Button mButtonEnviarReclamacao;
    private Button mButtonImg;
    private Uri mSelectUri;
    private ImageView mFotoComprovante;
    private String dataReclamacao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reclamacao);


        //CRIANDO REFERÊNCIAS
        SimpleDateFormat formataData = new SimpleDateFormat("dd-MM-yyyy");
        Date data = new Date();
        dataReclamacao = formataData.format(data);
        mEditDataAtendimento = findViewById(R.id.edit_dataAtendimento);
        mEditCnpjPosto = findViewById(R.id.edit_cnpjPosto);
        mEditEmail = findViewById(R.id.edit_emailCliente);
        mEditMotivo = findViewById(R.id.edit_motivoReclamacao);
        mButtonImg = findViewById(R.id.bt_comprovante);
        mFotoComprovante = findViewById(R.id.img_comprovanteAtendimento);
        mButtonImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selecionarFoto();
            }
        });
        mButtonEnviarReclamacao = findViewById(R.id.bt_enviarReclamacao);
        mButtonEnviarReclamacao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registrarReclamacao();
            }
        });
    }

    private void selecionarFoto() {
        //ABRINDO A GALERIA DE FOTOS
        Intent intent = new Intent(Intent.ACTION_PICK);
        //TIPO DE DADOS QUE VOU PERMITIR A INSERÇÃO
        intent.setType("image/*");
        startActivityForResult(intent, 0);
    }


    private void registrarReclamacao() {
        //PEGANDO DADOS INFORMADOS NO FORMULARIO
        final String dataRec = dataReclamacao;
        final String dataAtend = mEditDataAtendimento.getText().toString();
        final String cnpjPosto = mEditCnpjPosto.getText().toString();
        final String emailCliente = mEditEmail.getText().toString();
        final String motivo = mEditMotivo.getText().toString();

        //GARANTINDO PREENCHIMENTO DOS DADOS OBRIGATÓRIOS E VALIDAÇÕES DE FORMULÁRIO
        if (dataAtend == null || dataAtend.isEmpty()) {
            Toast.makeText(this, "Campo DATA DO ATENDIMENTO é obrigatório e deve ser informado!", Toast.LENGTH_SHORT).show();
            return;
        }
        if (cnpjPosto == null || cnpjPosto.isEmpty()) {
            Toast.makeText(this, "Campo CNPJ DO POSTO é obrigatório e deve ser informado!", Toast.LENGTH_SHORT).show();
            return;
        }
        if (emailCliente == null || emailCliente.isEmpty()) {
            Toast.makeText(this, "Campo EMAIL é obrigatório e deve ser informado!", Toast.LENGTH_SHORT).show();
            return;
        }
        //VALIDANDO SE O E-MAIL INFORMADO É VALIDO
        if (android.util.Patterns.EMAIL_ADDRESS.matcher(emailCliente).matches() != true) {
            Toast.makeText(this, "Formato de e-mail inválido!", Toast.LENGTH_SHORT).show();
            return;
        }
        if (motivo == null || motivo.isEmpty()) {
            Toast.makeText(this, "Campo MOTIVO DO ATENDIMENTO é obrigatório e deve ser informado!", Toast.LENGTH_SHORT).show();
            return;
        }

        //CRIANDO A HASH PARA REFERÊNCIA DO NOME DO ARQUIVO
        String fileName = UUID.randomUUID().toString();
        final StorageReference ref = FirebaseStorage.getInstance().getReference("/images/comprovantes/" + fileName);
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
                                String profileUrl = uri.toString();
                                //RECLAMAÇÃO A SER REGISTRADA
                                final Denuncia denuncia = new Denuncia(dataRec, dataAtend, uri.toString(), emailCliente, motivo, cnpjPosto);
                                //CRIANDO A REFERÂNCIA PARA UMA COLEÇÃO DE RECLAMACOES
                                FirebaseFirestore.getInstance().collection("denuncias")
                                        .add(denuncia)
                                        .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                            @Override
                                            public void onSuccess(DocumentReference documentReference) {
                                                Log.d("TESTE", "reclamação contra o posto " + denuncia.getPosto() + "registrada!");
                                            }
                                        }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Log.e("ERRO", "não foi possível registrar a reclamação!");
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