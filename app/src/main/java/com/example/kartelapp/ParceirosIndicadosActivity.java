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

import java.util.List;

public class ParceirosIndicadosActivity extends AppCompatActivity {

    private GroupAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.parceiros_indicados_activity);
        fetchParceiros();
        RecyclerView rv = findViewById(R.id.rv_parceiros);
        adapter = new GroupAdapter();
        rv.setLayoutManager(new LinearLayoutManager(this));
        rv.setAdapter(adapter);
    }

    /**
     * CLASSE INTERNA RESPONSAVEL POR MANIPULAR ITENS QUE APARECERÃO NA RECYCLER VIEW LISTANDO OS PARCEIROS
     */
    private class ParceiroItem extends Item<ViewHolder> {
        private final Parceiro parceiro;


        private ParceiroItem(Parceiro p) {
            this.parceiro = p;
        }

        @Override
        public void bind(@NonNull ViewHolder viewHolder, int position) {
            //CONECTANDO AOS OBJETOS PARA PODER EDITAR SEUS VALORES
            TextView nomeparceiro = viewHolder.itemView.findViewById(R.id.vl_nomeParceiroItem);
            TextView endereco = viewHolder.itemView.findViewById(R.id.vl_enderecoParceiro);
            TextView bairro = viewHolder.itemView.findViewById(R.id.vl_bairroParceiro);
            TextView cidade = viewHolder.itemView.findViewById(R.id.vl_cidadeParceiro);
            ImageView imagem = viewHolder.itemView.findViewById(R.id.im_parceiro);
            TextView fone = viewHolder.itemView.findViewById(R.id.vl_foneParceiro);
            TextView servico = viewHolder.itemView.findViewById(R.id.vl_servicoItemParceiro);

            //POPULANDO DADOS DO ITEM COM O RESTANTE DOS ATRIBUTOS
            nomeparceiro.setText(parceiro.getParceiro());
            endereco.setText(parceiro.getEndereco());
            bairro.setText(parceiro.getBairro());
            cidade.setText(parceiro.getCidade());
            fone.setText(parceiro.getFone());
            servico.setText(parceiro.getAtividade());


            if(parceiro.getId().equalsIgnoreCase("102")) {
                Picasso.get()
                        .load("https://firebasestorage.googleapis.com/v0/b/kartel-59019.appspot.com/o/images%2Fparceiros%2Fborracharia.jpg?alt=media&token=8bb9345d-8835-4f1f-a9b5-b182dc9f5b7c")
                        .into(imagem);
            }
            if(parceiro.getId().equalsIgnoreCase("101")) {
                Picasso.get()
                        .load("https://firebasestorage.googleapis.com/v0/b/kartel-59019.appspot.com/o/images%2Fparceiros%2Fguincho.jpg?alt=media&token=ec401282-9dc1-4957-a3dc-598b1e5485eb")
                        .into(imagem);
            }
            if(parceiro.getId().equalsIgnoreCase("100")) {
                Picasso.get()
                        .load("https://firebasestorage.googleapis.com/v0/b/kartel-59019.appspot.com/o/images%2Fparceiros%2Fmecanica.jpg?alt=media&token=68551f32-72ee-44da-bbb0-fbac7af22dac")
                        .into(imagem);
            }

        }

        @Override
        public int getLayout() {
            return R.layout.item_parceiro;
        }

    }

    /**
     * BUSCANDO PARCEIROS NA BASE
     */
    private void fetchParceiros() {
        //CRIA UMA REFERENCIA PARA A COLEÇÃO DE PARCEIROS

        FirebaseFirestore.getInstance().collection("/parceiros")
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
                            Parceiro parceiro = doc.toObject(Parceiro.class);
                            adapter.add(new ParceiroItem(parceiro));
                        }
                    }
                });
    }
}
