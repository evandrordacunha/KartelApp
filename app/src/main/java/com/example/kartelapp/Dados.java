package com.example.kartelapp;


import android.app.Activity;

import androidx.annotation.NonNull;

import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.HashMap;
import java.util.Map;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

public class Dados {

    private final FirebaseFirestore db;
    private static final String TAG = "DOC";


    public Dados(FirebaseFirestore db) {
        this.db = db;
    }


    /**
     * VERIFICA A EXISTÊNCIA DE UM POSTO CADASTRADO NA BASE
     *
     * @param posto
     * @return
     */
    private boolean verificaPosto(Posto posto) {
        CollectionReference listaPostos = db.collection("postos");
        Query endereco = listaPostos.whereEqualTo("endereco", posto.getEndereco());
        if (endereco != null) {
            return true;
        }
        return false;
    }


    /**
     * MÉTODO RESPONSAVEL POR POPULAR A BASE DE DADOS LENDO O ARQUIVO CSV DISPONIBILIZADO PELA ANP E INSERINDO NO FIRESTORE
     *
     * @param activity
     */
    public void inserirPostos(Activity activity) {

        InputStream is = activity.getResources().openRawResource(R.raw.distribuidores2019);
        List<String[]> resultado = new ArrayList<>();
        BufferedReader reader = new BufferedReader(new InputStreamReader(is, Charset.forName("UTF-8")));
        String linha;
        try {
            reader.readLine();
            while ((linha = reader.readLine()) != null) {
                String[] coluna = linha.split(";");
                String cnpj = coluna[0];
                String nome = coluna[1];
                String endereco = coluna[2];
                String bairro = coluna[3];
                String cidade = coluna[4];
                String estado = coluna[5];
                String pais = coluna[6];
                String bandeira = coluna[7];
                String gasolinaComum = coluna[8];
                String gasolinaAditivada = coluna[9];
                String etanol = coluna[10];
                String diesel = coluna[11];
                String gnv = coluna[12];
                String latitude = coluna[13];
                String longitude = coluna[14];
                String dataColeta = coluna[15];

                //CRIANDO REFERENCIA DE POSTO DE COMBUSTIVEL
                final Posto posto = new Posto(nome, cnpj, bandeira, endereco, bairro, cidade, estado, pais, latitude, longitude, gasolinaAditivada, gasolinaComum, etanol, diesel, gnv, dataColeta);

                //PEGANDO A DATA DO SISTEMA PARA GERAR A ULTIMA ATUALIZAÇÃO
                SimpleDateFormat formataData = new SimpleDateFormat("dd-MM-yyyy");
                Date ultimaAtualizacao = new Date();
                String dataFormatada = formataData.format(ultimaAtualizacao);

                //ADICIONANDO O POSTO A BASE DO CLOUD FIRESTORE
                Map<String, Object> dado = new HashMap<>();

                dado.put("cnpj", posto.getCnpj().toUpperCase());
                dado.put("nome", posto.getNome().toUpperCase());
                dado.put("endereco", posto.getEndereco().toUpperCase());
                dado.put("bairro", posto.getBairro().toUpperCase());
                dado.put("cidade", posto.getCidade().toUpperCase());
                dado.put("estado", posto.getEstado().toUpperCase());
                dado.put("pais", posto.getPais().toUpperCase());
                dado.put("bandeira", posto.getBandeira().toUpperCase());
                dado.put("precoGasolinaComum", posto.getPrecoGasolinaComum().toUpperCase());
                dado.put("precoGasolinaAditivada", posto.getPrecoGasolinaAditivada().toUpperCase());
                dado.put("precoEtanol", posto.getPrecoEtanol().toUpperCase());
                dado.put("precoDiesel", posto.getPrecoDiesel().toUpperCase());
                dado.put("precoGnv", posto.getPrecoGnv().toUpperCase());

                dado.put("latitude", posto.getLatitude());
                dado.put("longitude", posto.getLongitude());
                dado.put("ultimaAtualizacao", dataFormatada);

                //INSERE POSTO  E CRIA DOCUMENTO BASEADO NO CNPJ SEM A MÁSCARA, APENAS NÚMEROS
                db.collection("postos").document(posto.getCnpj().replaceAll("\\D", ""))
                        .set(dado)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Log.e("INSERIDO!", "Posto " + posto.getNome() + " atualizado!");
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.e("Erro", "Não foi possível atualizar posto!");
                    }
                });
            }

        } catch (IOException ex) {
            throw new RuntimeException("Erro na leitura do CSV" + ex.getMessage());
        } finally {
            try {
                is.close();
            } catch (IOException e) {
                throw new RuntimeException("Erro ao finalizar o inputStream" + e.getMessage());
            }
        }
    }

    public void registrarReclamacao(final Denuncia denuncia) {
        //ADICIONANDO O POSTO A BASE DO CLOUD FIRESTORE
        Map<String, Object> dado = new HashMap<>();

        dado.put("comprovante", denuncia.getComprovante());
        dado.put("data", denuncia.getData());
        dado.put("dataAtendimento", denuncia.getDataAtendimento());
        dado.put("idUsuario", denuncia.getIdUsuario().toUpperCase());
        dado.put("posto", denuncia.getPosto());
        dado.put("motivo", denuncia.getMotivo().toUpperCase());


        //INSERE POSTO  E CRIA DOCUMENTO BASEADO NO CNPJ SEM A MÁSCARA, APENAS NÚMEROS
        db.collection("denuncias")
                .add(dado).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
            @Override
            public void onSuccess(DocumentReference documentReference) {
                Log.d("TESTE", "RECLAMAÇÃO SOBRE O POSTO " + denuncia.getPosto() + " REGISTRADA!");
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.e("ERRO", "não foi possível registrar a reclamação!");

            }
        });
    }


}


