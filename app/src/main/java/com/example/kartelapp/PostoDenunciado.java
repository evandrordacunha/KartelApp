package com.example.kartelapp;

import android.util.Log;

import androidx.annotation.Nullable;

import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class PostoDenunciado {

    private String data;
    private String dataAtendimento;
    private String idUsuario;
    private String motivo;
    private String posto;
    private String comprovante;
    private ArrayList<Posto> postosNaBase = new ArrayList<>();
    private ArrayList<Denuncia> denunciasNaBase = new ArrayList<>();

    public PostoDenunciado(String data, String dataAtendimento, String idUsuario, String motivo, String posto, String comprovante) {
        this.data = data;
        this.dataAtendimento = dataAtendimento;
        this.idUsuario = idUsuario;
        this.motivo = motivo;
        this.posto = posto;
        this.comprovante = comprovante;
    }

    public PostoDenunciado() {
    }

    public String getData() {
        return data;
    }

    public String getDataAtendimento() {
        return dataAtendimento;
    }

    public String getIdUsuario() {
        return idUsuario;
    }

    public String getMotivo() {
        return motivo;
    }

    public String getPosto() {
        return posto;
    }

    public String getComprovante() {
        return comprovante;
    }

    /**
     * POPULA A LISTA COM OS POSTOS DA BASE
     */
    private void carregarPostos() {

        //CRIA UMA REFERENCIA PARA A COLEÇÃO DE POSTOS
        FirebaseFirestore.getInstance().collection("/postos")
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                        //VERIFICANDO SE ENCONTROU ALGUMA EXCEÇÃO CAPAZ DE IMPEDIR A EXECUÇÃO, CASO ENCONTRE, PARE A APLICAÇÃO
                        if (e != null) {
                            Log.e("TESTE", "Erro: ", e);
                            return;
                        }
                        //REFERÊNCIA PARA DENUNCIAS
                        List<DocumentSnapshot> documentos = queryDocumentSnapshots.getDocuments();
                        for (DocumentSnapshot doc : documentos) {
                            Posto posto = doc.toObject(Posto.class);
                            postosNaBase.add(posto);
                            Log.d("TESTE", posto.getNome());
                        }
                    }
                });

    }

    /**
     * POPULA A LISTA COM DENUNCIAS DA BASE
     */
    public void carregarDenuncias() {
        //CRIA UMA REFERENCIA PARA A COLEÇÃO DE POSTOS
        FirebaseFirestore.getInstance().collection("/denuncias")
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                        //VERIFICANDO SE ENCONTROU ALGUMA EXCEÇÃO CAPAZ DE IMPEDIR A EXECUÇÃO, CASO ENCONTRE, PARE A APLICAÇÃO
                        if (e != null) {
                            Log.e("TESTE", "Erro: ", e);
                            return;
                        }
                        //REFERÊNCIA PARA DENUNCIAS
                        List<DocumentSnapshot> documentos = queryDocumentSnapshots.getDocuments();
                        for (DocumentSnapshot doc : documentos) {
                            Denuncia denuncia = doc.toObject(Denuncia.class);
                            denunciasNaBase.add(denuncia);
                            //   Log.d("TESTE", denuncia.getDataCadastro());
                        }
                    }
                });
    }


    /**
     * @return HashMap tendo ocmo chave o posto e valor a quantidade de denuncias
     */
    private HashMap<Posto, Integer> buscarPostosMaisDenunciados() {
        HashMap<Posto, Integer> postosEdenuncias = new HashMap<>();


        for (int i = 0; i < postosNaBase.size(); i++) {
            Posto posto = postosNaBase.get(i);
            int count = 0;
            for (int j = 0; j < denunciasNaBase.size(); j++) {
                Denuncia denuncia = denunciasNaBase.get(j);
                if (denuncia.getPosto() == posto.getCnpj()) {
                    count++;
                    Log.d("TESTE", "Posto" + posto.getCnpj() + "" + count);
                }

            }
            postosEdenuncias.put(posto, count);
        }

        return postosEdenuncias;
    }

}
