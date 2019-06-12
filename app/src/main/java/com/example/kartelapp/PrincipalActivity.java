package com.example.kartelapp;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.os.Build;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.navigation.NavigationView;

import androidx.core.app.ActivityCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.provider.Settings;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

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
        implements NavigationView.OnNavigationItemSelectedListener,LocationManager,LocationListener, OnMapReadyCallback {


    private static final String TAG = "TESTE";
    private double latitudeClient;
    private double longitudeClient;
    private Location location;
    private static ArrayList<Posto> listaPostos = new ArrayList<>();
    private GroupAdapter adapter;
    private Button mButtonVerMapa;
    private GoogleMap mMap;
    private Marker localizaoUsuario;
    private Marker localizaoPosto;
    private LatLng latLng;
    private double userLatitude;
    private double userLongitude;
    private final FirebaseFirestore db = FirebaseFirestore.getInstance();
    private List<Posto> postosProximos = new ArrayList<>();



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

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

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
        rv.setLayoutManager(new LinearLayoutManager(this));
        rv.setAdapter(adapter);
        buscarLocalizacaoGps();
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
     * MÉTODO USADO PARA SOLICITAR PEDIDO DE PERMISSÃO DE USO DO GPS AO USUARIO
     */
    @Override
    public void buscarLocalizacaoGps() {

        android.location.LocationManager lm = (android.location.LocationManager) getSystemService(Context.LOCATION_SERVICE);


        boolean isGPS = lm.isProviderEnabled(android.location.LocationManager.GPS_PROVIDER);
        boolean isNetwork = lm.isProviderEnabled(android.location.LocationManager.NETWORK_PROVIDER);
        boolean autorizado = true;
        int ALL_PERMISSIONS_RESULT = 101;
        //Distancia em metros para calculo do raio de precisão
        long MIN_DISTANCE_CHANGE_FOR_UPDATES = 10;
        // Tempo de atualização em milesegundos
        long MIN_TIME_BW_UPDATES = 1000 * 10;

        ArrayList<String> permissoes = new ArrayList<>();
        ArrayList<String> pedidosPermissoes;

        permissoes.add(android.Manifest.permission.ACCESS_FINE_LOCATION);
        permissoes.add(android.Manifest.permission.ACCESS_COARSE_LOCATION);
        pedidosPermissoes = encontrarPermissoesNecessarias(permissoes);

        //Verifica se o GPS e a Rede estão ligados, se não pedir ao usuário para ligar
        if (!isGPS && !isNetwork) {
            exibirAlertas();

        } else {
            // Confere as permissões

            // verifica se a versão do android é maior ou igual  que a Marshmallow, pois aí precisa pedir permissão
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (pedidosPermissoes.size() > 0) {
                    requestPermissions(pedidosPermissoes.toArray(new String[pedidosPermissoes.size()]),
                            ALL_PERMISSIONS_RESULT);
                    autorizado = false;
                }
            }
        }

        //Verifica se foi concedida permissão para o local FINE LOCATION e COARSE
        if (ActivityCompat.checkSelfPermission(this,
                android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION)
                        != PackageManager.PERMISSION_GRANTED) {

            Toast.makeText(this, "Permissão negada!", Toast.LENGTH_SHORT).show();
            return;
        }

        //Valida se localizão é valida e pede a localização
        if (autorizado) {
            if (isGPS) {
                lm.requestLocationUpdates(
                        android.location.LocationManager.GPS_PROVIDER,
                        MIN_TIME_BW_UPDATES,
                        MIN_DISTANCE_CHANGE_FOR_UPDATES, (LocationListener) this);
            } else if (isNetwork) {
                // localização de rede

                lm.requestLocationUpdates(
                        android.location.LocationManager.NETWORK_PROVIDER,
                        MIN_TIME_BW_UPDATES,
                        MIN_DISTANCE_CHANGE_FOR_UPDATES, (LocationListener) this);
            }
        } else {
            Toast.makeText(this, "Não foi possível obter a localização do usuário!", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * MÉTODO RESPONSÁVEL POR REALIZAR INTERAÇÃO COM O USUÁRIO PARA SOLICITAR ATIVAÇÃO DO GPS COMO MANDA BOAS PRÁTICAS DO GOOGLE
     */
    @Override
    public void exibirAlertas() {
        AlertDialog.Builder alerta = new AlertDialog.Builder(this);
        alerta.setTitle("GPS desativado!");
        alerta.setMessage("Ativar GPS ?");
        alerta.setPositiveButton("SIM", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivity(intent);
            }
        });
        alerta.setNegativeButton("NÃO", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        alerta.show();
    }


    /**
     * MÉTODO RESPONSÁVEL POR VERIFICAR SE EXISTE ALGUMA SOLICITAÇÃO POR PERMISSÃO NECESSÁRIA E NÃO SOLICITADA
     *
     * @param solicitacao
     * @return
     */
    @Override
    public ArrayList encontrarPermissoesNecessarias(ArrayList<String> solicitacao) {
        ArrayList resultado = new ArrayList();
        for (String permissao : solicitacao) {
            if (!verificaPermissao(permissao)) {
                resultado.add(permissao);
            }
        }
        return resultado;
    }


    /**
     * OS DOIS MÉTODOS ABAIXO SÃO USADOS PARA VALIDAR O PEDIDO DE PERMISSAO
     *
     * @param permissao
     * @return
     */
    @Override
    public boolean verificaPermissao(String permissao) {
        if (validaPedidoPermissao()) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                return (checkSelfPermission(permissao) == PackageManager.PERMISSION_GRANTED);
            }
        }
        return true;
    }
    @Override
    public boolean validaPedidoPermissao() {
        return (Build.VERSION.SDK_INT > Build.VERSION_CODES.CUR_DEVELOPMENT);
    }

    @Override
    public void onLocationChanged(Location location) {
        //LOCALIZAÇÃO DO USUÁRIO PARA GERENCIAR O MÉTODO RESPONSÁVEL POR CALCULAR DISTANCIA EM METROS
        userLatitude = location.getLatitude();
        userLongitude = location.getLongitude();

        //VERIFICA SE MARCADOR JÁ EXISTE, SE JA EXISTE, REMOVE REMOVE O ATUAL PARA INCLUIR UM NOVO
        if (localizaoUsuario != null) {
            localizaoUsuario.remove();
        }
        //ATUALIZA LATITUDE E LONGITUDE
        //latLng = new LatLng(location.getLatitude(), location.getLongitude());
        latLng = new LatLng(-30.0581445,-51.1767154);

        //GERENCIANDO PROPRIEDADES DO MARCADOR A SER ADICIONADO
        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(latLng);
        markerOptions.title("Minha localização");
        markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE));


        localizaoUsuario = mMap.addMarker(markerOptions);

        //CENTRALIZANDO A CAMERA NO NOVO MARCADOR APONTANDO PARA A LOCALIZAÇÃO ATUAL DO USUARIO
        CameraPosition cameraPosition = new CameraPosition.Builder().zoom(15).target(latLng).build();
        mMap.moveCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
        Toast.makeText(this, "Localização atualizada!", Toast.LENGTH_SHORT).show();

        //MARCADORES A SEREM ADICIONADOS

        for(int i = 0; i < postosProximos.size();i++){
            Posto p = postosProximos.get(i);

            double lat = Double.parseDouble(p.getLatitude());
            //Log.d("LATITUDE",p.getLatitude());
            double lon = Double.parseDouble(p.getLongitude());


            LatLng posto = new LatLng(lat, lon);
            mMap.addMarker(new MarkerOptions().position(posto).title(p.getNome()));
            mMap.setContentDescription("Gasolina: R$ "+p.getPrecoGasolinaComum());
            mMap.moveCamera(CameraUpdateFactory.newLatLng(posto));

        }
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
       //REFERENCIA PARA UM MAPA
        mMap = googleMap;
        carregarPostos();



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
            TextView distancia = viewHolder.itemView.findViewById(R.id.v_vlDistancia);

            double latitudePosto = Double.parseDouble(posto.getLatitude());
            double  longitudePosto = Double.parseDouble(posto.getLongitude());
            distancia.setText(""+calcularDistanciaEmMetros(userLatitude,userLongitude,latitudePosto,longitudePosto));


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


    /**
     * MÉTODO RESPONSÁVEL POR CALCULAR A DISTANCIA ENTRE DOIS PONTOS EM LINHA RETA USANDO A FÓRMULA DE HAVERSINE
     *
     * @param minhaLatitude
     * @param minhaLongitude
     * @param latitudePosto
     * @param longitudePosto
     * @return
     */
    private double calcularDistanciaEmMetros(double minhaLatitude, double minhaLongitude, double latitudePosto, double longitudePosto) {

        final int raioTerrestre = 6371;
        double userLatitude = Math.toRadians(latitudePosto - minhaLatitude);
        double userLongitude = Math.toRadians(longitudePosto - minhaLongitude);
        double postoLat = Math.sin(userLatitude / 2);
        double postoLong = Math.sin(userLongitude / 2);
        double a = Math.pow(postoLat, 2) + Math.pow(postoLong, 2) * Math.cos(Math.toRadians(minhaLatitude)) * Math.cos(Math.toRadians(latitudePosto));
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        double distancia = raioTerrestre * c;
        return distancia * 1000; //distância em metros
    }

    /**
     * POPULANDO MARCADORES
     */
    public void carregarPostos() {
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
                            postosProximos.add(posto);
                        }
                    }
                });
    }

    /**
     * BUSCA POSTOS INSERIDOS NO FIRESTORE E ADICIONA NO MAPA SEUS MARCADORES
     */

    private void adicionarMarcadores() {


        final FirebaseFirestore db = FirebaseFirestore.getInstance();

        db.collection("/postos")
                .whereEqualTo("estado", "RS")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (final QueryDocumentSnapshot document : task.getResult()) {
                                final FirebaseFirestore db = FirebaseFirestore.getInstance();

                                DocumentReference docRef = db.collection("/postos").document(document.getId());
                                docRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                    @Override
                                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                                        Posto p = documentSnapshot.toObject(Posto.class);
                                        //AQUI É FEITO O CALCULO DA DISTANCIA EM METROS ENTRE O USUÁRIO E O POSTO IMPORTADO DA BASE

                                        double lat = Double.parseDouble(p.getLatitude());
                                        double lon = Double.parseDouble(p.getLongitude());


                                        double distanciaCalculada = calcularDistanciaEmMetros(userLatitude, userLongitude, lat, lon);
                                        //SE O POSTO TIVER ATÉ 2KM PRÓXIMO AO USUÁRIO ENTÃO EXIBE O MARCADOR
                                        if (distanciaCalculada <= 2000) {
                                            //ADICIONA MARCADORES E MOVE A CAMERA
                                            PrincipalActivity principalActivity = new PrincipalActivity();
                                            String nomePosto = p.getNome();
                                            String distanciaPosto = "" + distanciaCalculada;
                                            mMap.addMarker(new MarkerOptions().position(latLng).title(p.getNome()));
                                            CameraPosition cameraPosition = new CameraPosition.Builder().zoom(15).build();
                                            mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
                                            Log.d("TESTE", p.getNome() + " marcado!");
                                        }
                                    }
                                });

                            }
                        } else {
                            Log.d("TESTE", "Erro ao ler dados do Firestore!", task.getException());
                        }
                    }
                });
    }


}
