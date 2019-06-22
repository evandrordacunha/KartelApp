package com.example.kartelapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;
import com.xwray.groupie.GroupAdapter;
import com.xwray.groupie.Item;
import com.xwray.groupie.ViewHolder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PostosMaisDenunciadosActivity extends AppCompatActivity {

    private GroupAdapter adapter;
    private ArrayList<Posto> postosNaBase = new ArrayList<>();
    private ArrayList<Denuncia> denunciasNaBase = new ArrayList<>();
    private HashMap<Posto, Integer> totalDenunciasPostos = new HashMap<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_postos_mais_denunciados);
        RecyclerView rv = findViewById(R.id.rv_postosDenunciados);
        adapter = new GroupAdapter();
        rv.setLayoutManager(new LinearLayoutManager(this));
        rv.setAdapter(adapter);
        fetchPostos();
        carregarPostosComDenuncias();
        adapter.add(new DenunciaItem());


    }


    /**
     * CLASSE INTERNA RESPONSAVEL POR MANIPULAR ITENS QUE APARECERÃO NA RECYCLER VIEW LISTANDOS OS POSTOS
     */
    private class DenunciaItem extends Item<ViewHolder> {


        private DenunciaItem() {

        }

        @Override
        public void bind(@NonNull ViewHolder viewHolder, int position) {

            //CONECTANDO AOS OBJETOS PARA PODER EDITAR SEUS VALORES
            TextView posto = viewHolder.itemView.findViewById(R.id.vl_postoDenunciado);
            TextView endereco = viewHolder.itemView.findViewById(R.id.vl_enderecoPostoDenunciado);
            TextView bairro = viewHolder.itemView.findViewById(R.id.vl_bairroPostoDenunciado);
            TextView cidade = viewHolder.itemView.findViewById(R.id.vl_cidadePostoDenunciado);
            TextView cnpj = viewHolder.itemView.findViewById(R.id.vl_cnpjPostoDenunciado);
            TextView reclamacoes = viewHolder.itemView.findViewById(R.id.vl_totalReclamacoes);

            for (Map.Entry<Posto, Integer> entrada : totalDenunciasPostos.entrySet()) {

                Posto p = entrada.getKey();
                int totalReclamacoes = entrada.getValue();
                //POPULANDO DADOS DO ITEM COM O RESTANTE DOS ATRIBUTOS
                posto.setText(p.getNome());
                endereco.setText(p.getEndereco());
                bairro.setText(p.getBairro());
                cidade.setText(p.getCidade());
                cnpj.setText(p.getCnpj());
                reclamacoes.setText(String.valueOf(totalReclamacoes));
            }

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


                            Log.d("TESTE", posto.getNome());
                            postosNaBase.add(posto);
                        }

                        fetchDenuncia();
                    }
                });
    }

    /**
     * PERCORRE LISTA DE POSTOS DA BASE E VERIFICA SE OS POSTOS COM DENUNCIAS E CARREGA PARA UMA NOVA LISTA
     */
    private void carregarPostosComDenuncias() {
        Posto posto = null;
        Denuncia denuncia = null;

        for(int i=0; i< postosNaBase.size();i++){
            //referenciando um posto da base
            posto = postosNaBase.get(i);
            int count = 0; //contador de denuncias

            for(int j =0; j< denunciasNaBase.size();j++){
                //referenciando uma denuncia
                denuncia = denunciasNaBase.get(j);
                if(posto.getCnpj() == denuncia.getPosto()) {
                    Log.d("TESTE POSTO ( "+i+")", posto.getCnpj());
                    Log.d("TESTE DENUNCIA ( "+j+")", denuncia.getPosto());
                    count++;
                }
            }
            Log.d("TESTE","Posto  " +posto.getNome() +"com  "+count +" denuncias");
            totalDenunciasPostos.put(posto,count);
        }

        adapter.notifyDataSetChanged();

    }
}
