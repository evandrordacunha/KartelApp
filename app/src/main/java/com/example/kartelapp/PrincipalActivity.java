package com.example.kartelapp;

import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.google.android.material.navigation.NavigationView;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.squareup.picasso.Picasso;
import com.xwray.groupie.GroupAdapter;
import com.xwray.groupie.Item;
import com.xwray.groupie.ViewHolder;

import java.util.ArrayList;
import java.util.List;

public class PrincipalActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {


    private static final String TAG = "TESTE";
    private double latitudeClient;
    private double longitudeClient;
    private static ArrayList<Posto> listaPostos = new ArrayList<>();
    private GroupAdapter adapter;
    //Button btnLocalizarMapa;


    public double getLatitudeClient() {
        return latitudeClient;
    }

    public void setLatitudeClient(double latitudeClient) {
        this.latitudeClient = latitudeClient;
    }

    public double getLongitudeClient() {
        return longitudeClient;
    }

    public void setLongitudeClient(double longitudeClient) {
        this.longitudeClient = longitudeClient;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_principal);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        //CONFIGURANDO ADAPTER PARA EXIBIÇÃO DA LISTA POSTOS NA RECYCLER VIEW
        RecyclerView rv = findViewById(R.id.rv_listaPostos);
        adapter = new GroupAdapter();
        rv.setAdapter(adapter);
        rv.setLayoutManager(new LinearLayoutManager(this));
        fetchPostos();

    }


    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.principal, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //ACTION PARA FAZER LOGOFF DA APLICAÇÃO
        if (id == R.id.action_settings) {
            FirebaseAuth.getInstance().signOut();
            Intent intent = new Intent(this,LoginActivity.class);
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        //ITEM 1 DO MENU É DIRECIONADO PARA A ACTIVITY INSTITUCIONAL
        if (id == R.id.nav_camera) {
            Intent intent = new Intent(getApplicationContext(), about_fragment.class);
            startActivity(intent);
        } else if (id == R.id.nav_gallery) {
            Intent intent = new Intent(getApplicationContext(), PostosConfiaveisActivity.class);
            startActivity(intent);

        } else if (id == R.id.nav_slideshow) {
            Intent intent = new Intent(getApplicationContext(), DenunciaActivity.class);
            startActivity(intent);

        } else if (id == R.id.nav_manage) {
            Intent intent = new Intent(getApplicationContext(), ParceirosIndicadosActivity.class);
            startActivity(intent);

        } else if (id == R.id.nav_share) {
            Intent intent = new Intent(getApplicationContext(), CompartilharActivity.class);
            startActivity(intent);

        } else if (id == R.id.nav_send) {
            Intent intent = new Intent(getApplicationContext(), ContatoActivity.class);
            startActivity(intent);

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    /**
     * MÉTODO RESPONSAVEL POR LISTAR TODOS POSTOS DE COMBUSTIVEIS DO ESTADO DO RS CADASTRADOS NA BASE
     * ALIMENTANDO UMA LISTA LOCAL EM MEMÓRIA  DISPONIBILIZADA PARA AUXILIAR OUTRAS ATIVIDADES DA APLICAÇÃO
     */
    private List buscaPostos() {

        final FirebaseFirestore db = FirebaseFirestore.getInstance();

        db.collection("postos")
                .whereEqualTo("estado", "RS")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (final QueryDocumentSnapshot document : task.getResult()) {
                                final FirebaseFirestore db = FirebaseFirestore.getInstance();

                                DocumentReference docRef = db.collection("postos").document(document.getId());
                                docRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                    @Override
                                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                                        Posto p = documentSnapshot.toObject(Posto.class);
                                        listaPostos.add(p);
                                        Log.d(TAG, document.getId() + " adicionado na lista!");
                                    }
                                });

                            }
                        } else {
                            Log.d(TAG, "Erro ao ler dados do Firestore!", task.getException());
                        }
                    }});
        return listaPostos;
    }

    private String imprimirPostos() {
        String s = "";
        for (int i = 0; i < listaPostos.size(); i++) {
            s = s + listaPostos.get(i).toString() + "\n";
            Log.d("Posto:  ", listaPostos.get(i).getNome()+" está na lista!");

        }
        return "Postos cadastrados  " + s;
    }

    /**
     * BUSCANDO POSTOS DO FIREBASE PARA POPULAR O RECYCLER VIEW NA TELA PRINCIPAL
     */
    private void fetchPostos(){
        //CRIA UMA REFERENCIA PARA A COLEÇÃO DE POSTOS
        FirebaseFirestore.getInstance().collection("/postos")
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                        //VERIFICANDO SE ENCONTROU ALGUMA EXCEÇÃO CAPAZ DE IMPEDIR A EXECUÇÃO, CASO ENCONTRE, PARE A APLICAÇÃO
                        if(e != null){
                            Log.e("TESTE","Erro: ",e);
                            return;
                        }

                        //REFERÊNCIA PARA TODOS POSTOS DA BASE
                        List <DocumentSnapshot> documentos = queryDocumentSnapshots.getDocuments();

                        for (DocumentSnapshot doc:documentos) {
                          Posto posto =  doc.toObject(Posto.class);
                            Log.d("TESTE",posto.getNome());
                            adapter.add(new PostoItem(posto));
                        }
                    }
                });
    }

    /**
     * CLASSE INTERNA RESPONSAVEL POR MANIPULAR ITENS QUE APARECERÃO NA RECYCLER VIEW LISTANDOS OS POSTOS
     */
    private  class PostoItem extends Item<ViewHolder> {
        private final Posto posto;

        private PostoItem(Posto posto) {
            this.posto = posto;
        }

        @Override
        public void bind(@NonNull ViewHolder viewHolder, int position) {
            //CONECTANDO AOS OBJETOS PARA PODER EDITAR SEUS VALORES
            TextView nome = viewHolder.itemView.findViewById(R.id.v_razao_social);
            TextView endereco = viewHolder.itemView.findViewById(R.id.v_endereco);
            TextView bairro = viewHolder.itemView.findViewById(R.id.v_bairro);
            TextView cidade = viewHolder.itemView.findViewById(R.id.v_cidade);
            TextView gasolinaComum = viewHolder.itemView.findViewById(R.id.v_vlComum);
            TextView gasolinaAditivada = viewHolder.itemView.findViewById(R.id.v_vlAditivada);
            TextView etanol = viewHolder.itemView.findViewById(R.id.v_vlEtanol);
            TextView diesel = viewHolder.itemView.findViewById(R.id.v_vlDiesel);
            TextView gnv = viewHolder.itemView.findViewById(R.id.v_vlGnv);
            ImageView bandeiraIcone = viewHolder.itemView.findViewById(R.id.im_bandeira);

            //CARREGANDO IMAGEM DAS BANDEIRAS TESTANDO PAA CADA UM DOS POSTOS
            if(posto.getBandeira().equalsIgnoreCase("PETROBRAS")) {
                Picasso.get()
                        .load("https://firebasestorage.googleapis.com/v0/b/kartel-59019.appspot.com/o/images%2FbandeiraPetrobras.jpg?alt=media&token=d53b4fee-d299-4422-a9e7-43d034613ec5")
                        .into(bandeiraIcone);
            }
            if(posto.getBandeira().equalsIgnoreCase("IPIRANGA")) {
                Picasso.get()
                        .load("https://firebasestorage.googleapis.com/v0/b/kartel-59019.appspot.com/o/images%2FbandeiraIpiranga.jpg?alt=media&token=b0c55805-61cc-45f2-886f-51be07bb0fed")
                        .into(bandeiraIcone);
            }
            if(posto.getBandeira().equalsIgnoreCase("SHELL")) {
                Picasso.get()
                        .load("https://firebasestorage.googleapis.com/v0/b/kartel-59019.appspot.com/o/images%2FbandeiraShell.jpg?alt=media&token=4874b289-f988-4dce-aa09-7011f0e1db9e")
                        .into(bandeiraIcone);
            }
            if(posto.getBandeira().equalsIgnoreCase("MEGAPETRO")) {
                Picasso.get()
                        .load("https://firebasestorage.googleapis.com/v0/b/kartel-59019.appspot.com/o/images%2FbandeiraMegapetro.jpg?alt=media&token=71eed8b5-05f4-4e81-9a40-e4081bcf8de6")
                        .into(bandeiraIcone);
            }
            if(posto.getBandeira().equalsIgnoreCase("BRANCA")){
                Picasso.get()
                        .load("https://firebasestorage.googleapis.com/v0/b/kartel-59019.appspot.com/o/images%2FbandeiraBranca.jpg?alt=media&token=bee27663-f1a9-48bc-9243-6856f95929f7")
                        .into(bandeiraIcone);
            }

            //POPULANDO DADOS DO ITEM COM O RESTANTE DOS ATRIBUTOS
            nome.setText(posto.getNome());
            endereco.setText(posto.getEndereco());
            bairro.setText(posto.getBairro());
            cidade.setText(posto.getCidade());
            gasolinaComum.setText(posto.getPrecoGasolinaComum());
            gasolinaAditivada.setText(posto.getPrecoGasolinaAditivada());
            etanol.setText(posto.getPrecoEtanol());
            diesel.setText(posto.getPrecoDiesel());
            gnv.setText(posto.getPrecoGnv());
        }

        @Override
        public int getLayout() {
            return R.layout.item_posto;
        }
    }

}
