package com.example.kartelapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;
import com.squareup.picasso.Picasso;
import com.xwray.groupie.GroupAdapter;
import com.xwray.groupie.Item;
import com.xwray.groupie.ViewHolder;

import java.util.ArrayList;
import java.util.List;

public class PostosMaisDenunciadosActivity extends AppCompatActivity {

    private GroupAdapter adapter;
    private ArrayList<Denuncia> denunciasNaBase = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        fetchDenuncia();
        setContentView(R.layout.activity_postos_mais_denunciados);
        fetchPostos();
        RecyclerView rv = findViewById(R.id.rv_postosDenunciados);
        adapter = new GroupAdapter();
        rv.setLayoutManager(new LinearLayoutManager(this));
        rv.setAdapter(adapter);

    }


    /**
     * CLASSE INTERNA RESPONSAVEL POR MANIPULAR ITENS QUE APARECERÃO NA RECYCLER VIEW LISTANDOS OS POSTOS
     */
    private class DenunciaItem extends Item<ViewHolder> {
        private final Posto posto;
        private int count;

        private DenunciaItem(Posto p, int c) {
            this.posto = p;
            this.count = c;
        }

        @Override
        public void bind(@NonNull ViewHolder viewHolder, int position) {
            //CONECTANDO AOS OBJETOS PARA PODER EDITAR SEUS VALORES
            TextView nomePosto = viewHolder.itemView.findViewById(R.id.vl_nomeParceiroItem);
            TextView endereco = viewHolder.itemView.findViewById(R.id.vl_enderecoDenunciaItem);
            TextView bairro = viewHolder.itemView.findViewById(R.id.vl_bairroDenunciaItem);
            TextView cidade = viewHolder.itemView.findViewById(R.id.vc_cidadeDenunciaItem);
            ImageView bandeiraIcone = viewHolder.itemView.findViewById(R.id.im_bandeiraDenunciaItem);
            TextView cnpj = viewHolder.itemView.findViewById(R.id.vl_cnpjDenunciaItem);
            TextView reclacamacoes = viewHolder.itemView.findViewById(R.id.vl_totalReclamacoes);


            //CARREGANDO IMAGEM DAS BANDEIRAS TESTANDO PARA CADA UM DOS POSTOS
            if (posto.getBandeira().equalsIgnoreCase("PETROBRAS")) {
                Picasso.get()
                        .load("https://firebasestorage.googleapis.com/v0/b/kartel-59019.appspot.com/o/images%2FbandeiraPetrobras.jpg?alt=media&token=d53b4fee-d299-4422-a9e7-43d034613ec5")
                        .into(bandeiraIcone);
            }
            if (posto.getBandeira().equalsIgnoreCase("IPIRANGA")) {
                Picasso.get()
                        .load("https://firebasestorage.googleapis.com/v0/b/kartel-59019.appspot.com/o/images%2FbandeiraIpiranga.jpg?alt=media&token=b0c55805-61cc-45f2-886f-51be07bb0fed")
                        .into(bandeiraIcone);
            }
            if (posto.getBandeira().equalsIgnoreCase("SHELL")) {
                Picasso.get()
                        .load("https://firebasestorage.googleapis.com/v0/b/kartel-59019.appspot.com/o/images%2FbandeiraShell.jpg?alt=media&token=4874b289-f988-4dce-aa09-7011f0e1db9e")
                        .into(bandeiraIcone);
            }
            if (posto.getBandeira().equalsIgnoreCase("MEGAPETRO")) {
                Picasso.get()
                        .load("https://firebasestorage.googleapis.com/v0/b/kartel-59019.appspot.com/o/images%2FbandeiraMegapetro.jpg?alt=media&token=71eed8b5-05f4-4e81-9a40-e4081bcf8de6")
                        .into(bandeiraIcone);
            }
            if (posto.getBandeira().equalsIgnoreCase("BRANCA")) {
                Picasso.get()
                        .load("https://firebasestorage.googleapis.com/v0/b/kartel-59019.appspot.com/o/images%2FbandeiraBranca.jpg?alt=media&token=bee27663-f1a9-48bc-9243-6856f95929f7")
                        .into(bandeiraIcone);
            }

            //POPULANDO DADOS DO ITEM COM O RESTANTE DOS ATRIBUTOS
            nomePosto.setText(posto.getNome());
            endereco.setText(posto.getEndereco());
            bairro.setText(posto.getBairro());
            cidade.setText(posto.getCidade());
            cnpj.setText(posto.getCnpj());
            reclacamacoes.setText("" + count);

        }

        @Override
        public int getLayout() {
            return R.layout.item_denuncia;
        }

    }

    /**
     * BUSCANDO DENUNCIAS NA BASE
     */
    private void fetchDenuncia() {
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

                        //REFERÊNCIA PARA TODOS POSTOS DA BASE
                        List<DocumentSnapshot> documentos = queryDocumentSnapshots.getDocuments();


                        for (DocumentSnapshot doc : documentos) {
                            Denuncia denuncia = doc.toObject(Denuncia.class);
                            Log.d("TESTE", denuncia.getPosto() + "denunciado!");
                            denunciasNaBase.add(denuncia);
                        }
                    }
                });
    }

    /**
     * BUSCANDO POSTOS NA BASE
     */
    private void fetchPostos() {
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
                        //REFERÊNCIA PARA TODOS POSTOS DA BASE
                        List<DocumentSnapshot> documentos = queryDocumentSnapshots.getDocuments();


                        for (DocumentSnapshot doc : documentos) {
                            Posto posto = doc.toObject(Posto.class);
                            int cont = 0;
                            for (int i = 0; i < denunciasNaBase.size(); i++) {

                                if (denunciasNaBase.get(i).getPosto().equalsIgnoreCase(posto.getCnpj())) {
                                    Log.d("TESTE", "Posto  " + posto.getCnpj() + "denuncia  " + denunciasNaBase.get(i).getPosto());
                                    cont++;
                                }
                            }
                            if (cont > 0) {
                                adapter.add(new DenunciaItem(posto, cont));
                            }
                        }

                    }
                });
    }
}
