package com.example.kartelapp;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.provider.Settings;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback, LocationListener {

    private GoogleMap mMap;
    private Marker localizaoUsuario;
    private Marker localizaoPosto;
    private LatLng latLng;
    private double userLatitude;
    private double userLongitude;
    private final FirebaseFirestore db = FirebaseFirestore.getInstance();
    private List<Posto> postosProximos = new ArrayList<>();

    public double getUserLatitude() {
        return userLatitude;
    }

    public void setUserLatitude(double userLatitude) {
        this.userLatitude = userLatitude;
    }

    public double getUserLongitude() {
        return userLongitude;
    }

    public void setUserLongitude(double userLongitude) {
        this.userLongitude = userLongitude;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

/*

        // Add a marker in Sydney and move the camera
        LatLng sydney = new LatLng(-34, 151);
        mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
        mMap.setSnippet("Detalhes do marcador");
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
        */

/*
        PrincipalActivity principalActivity = new PrincipalActivity();
        // Add a marker in Sydney and move the camera
        LatLng usuario = new LatLng(principalActivity.getLatitudeClient(), principalActivity.getLongitudeClient());
        mMap.addMarker(new MarkerOptions().position(usuario).title("Minha Localização"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(usuario));
*/

        carregarPostos();
        buscarLocalizacaoGps();


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


    @Override
    public void onLocationChanged(Location location) {

        //VERIFICA SE MARCADOR JÁ EXISTE, SE JA EXISTE, REMOVE REMOVE O ATUAL PARA INCLUIR UM NOVO
        if (localizaoUsuario != null) {
            localizaoUsuario.remove();
        }
        //ATUALIZA LATITUDE E LONGITUDE
        //latLng = new LatLng(location.getLatitude(), location.getLongitude());
        latLng = new LatLng(-30.0581445,-51.1767154);
        //APROVEITA E ATUALIZA TAMBÉM LATITUDE E LONGITUDE SEPARADAS PARA SEREM USADAS POSTERIORMENTES EM OUTROS MÉTODOS
        userLatitude = location.getLatitude();
        userLongitude = location.getLongitude();
        //GERENCIANDO PROPRIEDADES DO MARCADOR A SER ADICIONADO
        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(latLng);
        markerOptions.title("Minha localização");
        markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE));
        localizaoUsuario = mMap.addMarker(markerOptions);

        //CENTRALIZANDO A CAMERA NO NOVO MARCADOR APONTANDO PARA A LOCALIZAÇÃO ATUAL DO USUARIO
        CameraPosition cameraPosition = new CameraPosition.Builder().zoom(15).target(latLng).build();
        mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
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

    /**
     * MÉTODO USADO PARA SOLICITAR PEDIDO DE PERMISSÃO DE USO DO GPS AO USUARIO
     */
    private void buscarLocalizacaoGps() {

        LocationManager lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        boolean isGPS = lm.isProviderEnabled(LocationManager.GPS_PROVIDER);
        boolean isNetwork = lm.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
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
                        LocationManager.GPS_PROVIDER,
                        MIN_TIME_BW_UPDATES,
                        MIN_DISTANCE_CHANGE_FOR_UPDATES, this);
            } else if (isNetwork) {
                // localização de rede

                lm.requestLocationUpdates(
                        LocationManager.NETWORK_PROVIDER,
                        MIN_TIME_BW_UPDATES,
                        MIN_DISTANCE_CHANGE_FOR_UPDATES, this);
            }
        } else {
            Toast.makeText(this, "Não foi possível obter a localização do usuário!", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * MÉTODO RESPONSÁVEL POR REALIZAR INTERAÇÃO COM O USUÁRIO PARA SOLICITAR ATIVAÇÃO DO GPS COMO MANDA BOAS PRÁTICAS DO GOOGLE
     */
    private void exibirAlertas() {
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
    private ArrayList encontrarPermissoesNecessarias(ArrayList<String> solicitacao) {
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
    private boolean verificaPermissao(String permissao) {
        if (validaPedidoPermissao()) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                return (checkSelfPermission(permissao) == PackageManager.PERMISSION_GRANTED);
            }
        }
        return true;
    }

    private boolean validaPedidoPermissao() {
        return (Build.VERSION.SDK_INT > Build.VERSION_CODES.CUR_DEVELOPMENT);
    }


    private void getMarkers() {


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
}