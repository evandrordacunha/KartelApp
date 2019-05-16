package com.example.kartelapp;


import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.annotation.WorkerThread;
import android.util.Log;
import android.widget.Toast;

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
import java.util.concurrent.Callable;
import java.util.concurrent.Executor;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldPath;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.WriteBatch;

public class Dados {

    private final FirebaseFirestore db;
    private static final String TAG = "DOC";


    public Dados(FirebaseFirestore db) {
        this.db = db;
    }

    /**
     * MÉTODO USADO PARA INSERIR DISTRIBUIDORES NO BANCO DE DADOS
     *
     * @param activity
     */
    public void populaDistribuidores(Activity activity) {

        //APONTANDO PARA A COLLECTION DISTRIBUIDORES CRIADA NO FIREBASE


        InputStream is = activity.getResources().openRawResource(R.raw.distribuidores2019);
        List<String[]> resultado = new ArrayList<>();
        BufferedReader reader = new BufferedReader(new InputStreamReader(is, Charset.forName("UTF-8")));
        String linha;
        try {
            reader.readLine();
            while ((linha = reader.readLine()) != null) {
                String[] coluna = linha.split(";");
                String identificador = coluna[0];
                String cnpj = coluna[2];
                String razaoSocial = coluna[1];
                String endereco = coluna[4];
                String complemento = coluna[5];
                String bairro = coluna[6];
                String latitude = coluna[8];
                String longitude = coluna[9];
                String uf = coluna[10];
                String municipio = coluna[7];
                String bandeira = coluna[3];
                //CRIANDO REFERENCIA DE POSTO DE COMBUSTIVEL
                final Posto posto = new Posto(identificador, razaoSocial, cnpj, bandeira, endereco, complemento, bairro, municipio, latitude, longitude, uf);

                //ADICIONANDO O POSTO A BASE DO CLOUD FIRESTORE

                Map<String, Object> dado = new HashMap<>();
                dado.put("id", posto.getIdentificador());
                dado.put("razao_social", posto.getRazaoSocial());
                dado.put("cnpj", posto.getCnpj());
                dado.put("bandeira", posto.getDistribuidora());
                dado.put("endereco", posto.getEndereco());
                dado.put("complemento", posto.getComplemento());
                dado.put("bairro", posto.getBairro());
                dado.put("municipio", posto.getMunicipio());
                dado.put("uf", posto.getUf());
                dado.put("latitude", posto.getLatitude());
                dado.put("longitude", posto.getLongitude());


                db.collection("distribuidores").document(posto.getIdentificador())
                        .set(dado)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {


                            }
                        }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {


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

    // [START delete_collection]

    /**
     * Delete all documents in a collection. Uses an Executor to perform work on a background
     * thread. This does *not* automatically discover and delete subcollections.
     */
    public Task<Void> deleteCollection(final CollectionReference collection,
                                       final int batchSize,
                                       Executor executor) {

        // Perform the delete operation on the provided Executor, which allows us to use
        // simpler synchronous logic without blocking the main thread.
        return Tasks.call(executor, new Callable<Void>() {
            @Override
            public Void call() throws Exception {
                // Get the first batch of documents in the collection
                Query query = collection.orderBy(FieldPath.documentId()).limit(batchSize);

                // Get a list of deleted documents
                List<DocumentSnapshot> deleted = deleteQueryBatch(query);

                // While the deleted documents in the last batch indicate that there
                // may still be more documents in the collection, page down to the
                // next batch and delete again
                while (deleted.size() >= batchSize) {
                    // Move the query cursor to start after the last doc in the batch
                    DocumentSnapshot last = deleted.get(deleted.size() - 1);
                    query = collection.orderBy(FieldPath.documentId())
                            .startAfter(last.getId())
                            .limit(batchSize);

                    deleted = deleteQueryBatch(query);
                }

                return null;
            }
        });

    }

    /**
     * Delete all results from a query in a single WriteBatch. Must be run on a worker thread
     * to avoid blocking/crashing the main thread.
     */
    @WorkerThread
    private List<DocumentSnapshot> deleteQueryBatch(final Query query) throws Exception {
        QuerySnapshot querySnapshot = Tasks.await(query.get());

        WriteBatch batch = query.getFirestore().batch();
        for (QueryDocumentSnapshot snapshot : querySnapshot) {
            batch.delete(snapshot.getReference());
        }
        Tasks.await(batch.commit());

        return querySnapshot.getDocuments();
    }
    // [END delete_collection]

    /**
     * MÉTODO USADO PARA INSERIR DISTRIBUIDORES NO BANCO DE DADOS
     *
     * @param activity
     */
    public void atualizaPrecos(Activity activity) {


        //RAZAO SOCIAL;ENDERECO;BAIRRO;CIDADE;ESTADO;PAIS;BANDEIRA;GASOLINA COMUM;GASOLINA ADITIVADA;ETANOL;DIESEL;GNV;LATITUDE;LONGITUDE;DATA COLETA;
        InputStream is = activity.getResources().openRawResource(R.raw.teste);
        List<String[]> resultado = new ArrayList<>();
        BufferedReader reader = new BufferedReader(new InputStreamReader(is, Charset.forName("UTF-8")));
        String linha;
        try {
            reader.readLine();
            while ((linha = reader.readLine()) != null) {
                String[] coluna = linha.split(";");
                String cnpj = coluna[0];
                String razaoSocial = coluna[1];
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
                final Distribuidor posto = new Distribuidor(cnpj, razaoSocial, endereco, bairro, cidade, estado, pais, bandeira, gasolinaComum, gasolinaAditivada, etanol, diesel, gnv, latitude, longitude, dataColeta);

                //PEGANDO A DATA DO SISTEMA PARA GERAR A ULTIMA ATUALIZAÇÃO
                SimpleDateFormat formataData = new SimpleDateFormat("dd-MM-yyyy");
                Date ultimaAtualizacao = new Date();
                String dataFormatada = formataData.format(ultimaAtualizacao);

                CollectionReference listaPostos = db.collection("postos");
                Query listaCnpj = listaPostos.whereEqualTo("Cnpj", posto.getCnpj());
                //ADICIONANDO O POSTO A BASE DO CLOUD FIRESTORE
                Map<String, Object> dado = new HashMap<>();


                //VARRE A COLEÇÃO PROCURANDO O CNPJ
                if (listaCnpj != null) {
                    Log.e("ERRO", "" + listaCnpj.toString());
                    //VALIDA SE ESTÁ SENDO INFORMADO ALGUM VALOR DE GASOLINA COMUM PARA SER ATUALIZADO
                    if (gasolinaComum != null) {
                        dado.put("Gasolina Comum: ", posto.getPrecoVendaGasComum());
                        Log.d("ATUALIZADO!", "Preço da Gasolina Comum atualizado!");
                    }
                    //VALIDA SE ESTÁ SENDO INFORMADO ALGUM VALOR DE GASOLINA ADITIV. PARA SER ATUALIZADO
                    if (gasolinaAditivada != null) {
                        dado.put("Gasolina Aditivada: ", posto.getPrecoVendaGasAditivada());
                        Log.d("ATUALIZADO!", "Preço da Gasolina aditivada atualizado!");
                    }
                    //VALIDA SE ESTÁ SENDO INFORMADO ALGUM VALOR DE ETANOL PARA SER ATUALIZADO
                    if (etanol != null) {
                        dado.put("Etanol: ", posto.getPrecoVendaEtanol());
                        Log.d("ATUALIZADO!", "Preço do Etanol atualizado!");
                    }
                    //VALIDA SE ESTÁ SENDO INFORMADO ALGUM VALOR DE DIESEL PARA SER ATUALIZADO
                    if (diesel != null) {
                        dado.put("Diesel: ", posto.getPrecoVendaDiesel());
                        Log.d("ATUALIZADO!", "Preço do Diesel atualizado!");
                    }
                    //VALIDA SE ESTÁ SENDO INFORMADO ALGUM VALOR DE GNV PARA SER ATUALIZADO
                    if (gnv != null) {
                        dado.put("Gnv: ", posto.getPrecoVendaGnv());
                        Log.d("ATUALIZADO!", "Preço da GNV atualizado!");
                    }
                } else {
                    dado.put("Cnpj: ", posto.getCnpj());
                    dado.put("Razao Social: ", posto.getRazaoSocial());
                    dado.put("Endereço: ", posto.getRazaoSocial());
                    dado.put("Bairro: ", posto.getBairro());
                    dado.put("Cidade: ", posto.getCidade());
                    dado.put("Estado: ", posto.getEstado());
                    dado.put("País: ", posto.getPais());
                    dado.put("Bandeira: ", posto.getBandeira());
                    dado.put("Gasolina Comum: ", posto.getPrecoVendaGasComum());
                    dado.put("Gasolina Aditivada: ", posto.getPrecoVendaGasAditivada());
                    dado.put("Etanol: ", posto.getPrecoVendaEtanol());
                    dado.put("Diesel: ", posto.getPrecoVendaDiesel());
                    dado.put("Gnv: : ", posto.getPrecoVendaGnv());
                    dado.put("Latitude: ", posto.getLatitude());
                    dado.put("Longitude: ", posto.getLongitude());
                    dado.put("Última Atualização: ", dataFormatada);
                }
                db.collection("postos").document(cnpj)
                        .set(dado)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Log.e("INSERIDO!", "Posto " + posto.getRazaoSocial() + " atualizado!");
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

    //VERIFICA A EXISTÊNCIA NA BASE DE UM POSTO CADASTRADO NO MESMO ENDERECO
    private boolean verificaPosto(Distribuidor posto) {
        CollectionReference listaPostos = db.collection("postos");
        Query endereco = listaPostos.whereEqualTo("endereco", posto.getEndereco());
        if (endereco != null) {
            return true;
        }
        return false;
    }


    public void inserirPostos(Activity activity) {

        //RAZAO SOCIAL;ENDERECO;BAIRRO;CIDADE;ESTADO;PAIS;BANDEIRA;GASOLINA COMUM;GASOLINA ADITIVADA;ETANOL;DIESEL;GNV;LATITUDE;LONGITUDE;DATA COLETA;
        InputStream is = activity.getResources().openRawResource(R.raw.distribuidores2019);
        List<String[]> resultado = new ArrayList<>();
        BufferedReader reader = new BufferedReader(new InputStreamReader(is, Charset.forName("UTF-8")));
        String linha;
        try {
            reader.readLine();
            while ((linha = reader.readLine()) != null) {
                String[] coluna = linha.split(";");
                String cnpj = coluna[0];
                String razaoSocial = coluna[1];
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
                final Distribuidor posto = new Distribuidor(cnpj, razaoSocial, endereco, bairro, cidade, estado, pais, bandeira, gasolinaComum, gasolinaAditivada, etanol, diesel, gnv, latitude, longitude, dataColeta);

                //PEGANDO A DATA DO SISTEMA PARA GERAR A ULTIMA ATUALIZAÇÃO
                SimpleDateFormat formataData = new SimpleDateFormat("dd-MM-yyyy");
                Date ultimaAtualizacao = new Date();
                String dataFormatada = formataData.format(ultimaAtualizacao);

                //ADICIONANDO O POSTO A BASE DO CLOUD FIRESTORE
                Map<String, Object> dado = new HashMap<>();

                dado.put("Cnpj", posto.getCnpj());
                dado.put("Razao Social", posto.getRazaoSocial());
                dado.put("Endereço", posto.getRazaoSocial());
                dado.put("Bairro", posto.getBairro());
                dado.put("Cidade", posto.getCidade());
                dado.put("Estado", posto.getEstado());
                dado.put("País:", posto.getPais());
                dado.put("Bandeira", posto.getBandeira());
                dado.put("Gasolina Comum", posto.getPrecoVendaGasComum());
                dado.put("Gasolina Aditivada", posto.getPrecoVendaGasAditivada());
                dado.put("Etanol", posto.getPrecoVendaEtanol());
                dado.put("Diesel", posto.getPrecoVendaDiesel());
                dado.put("Gnv", posto.getPrecoVendaGnv());
                dado.put("Latitude", posto.getLatitude());
                dado.put("Longitude", posto.getLongitude());
                dado.put("Última Atualização", dataFormatada);

                db.collection("postos")
                        .add(dado)
                        .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                            @Override
                            public void onSuccess(DocumentReference documentReference) {
                                Log.d("SUCESSO!", "Posto " + posto.getRazaoSocial() + " inserido com sucesso!");
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.e("ERRO!", "Não foi possível inserir " + posto.getRazaoSocial());
                    }
                });

            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}


