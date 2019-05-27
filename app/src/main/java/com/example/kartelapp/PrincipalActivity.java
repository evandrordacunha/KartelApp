package com.example.kartelapp;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationAvailability;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.location.SettingsClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class PrincipalActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    //CLIENTE PARA MANIPULAÇÃO DE EVENTOS
    FusedLocationProviderClient client;

    private static final String TAG = "TESTE";
    private static ArrayList<Posto> listaPostos = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_principal);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        /**
         FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
         fab.setOnClickListener(new View.OnClickListener() {
        @Override public void onClick(View view) {
        Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
        .setAction("Action", null).show();
        }
        });
         */
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        buscaPostos();

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        Query query = db.collection("distribuidores");


        client = LocationServices.getFusedLocationProviderClient(this);

        /*#########AQUI COMEÇA A IMPLEMENTAÇÃO E USO DA LOCATION API #####*/
        onResume();
        buscaPostos();
        imprimirPostos();


    }

    /**
     * GERENCIAMENTO DE GPS E RETORNO DE COORDENADAS DO USUÁRIO
     */
    @Override
    protected void onResume() {
        super.onResume();

        //VARIAVEL USADA PARA CONTROLAR O STATUS DO PLAY SERVICES NO DISPOSITIVO DO USUARIO
        int statusCode = GoogleApiAvailability.getInstance().isGooglePlayServicesAvailable(this);
        //FAZENDO O DOUBLE CHECK DO STATUS DO PLAY SERVICES
        switch (statusCode) {
            //AUSENTE
            case ConnectionResult.SERVICE_MISSING:
                //REQUER ATUALIZAÇÃO
            case ConnectionResult.SERVICE_VERSION_UPDATE_REQUIRED:
                //DISABILITADO
            case ConnectionResult.SERVICE_DISABLED:
                Log.d("TESTE", "show dialog");
                //CASO SEJA IDENTIFICADO QUE O PLAY SERVICES ESTÁ DESABILITADO, PEDE PARA HABILITAR OU MATA A APLICAÇÃO SE NÃO ATIVAR
                GoogleApiAvailability.getInstance().getErrorDialog(this, statusCode,
                        0, new DialogInterface.OnCancelListener() {
                            @Override
                            public void onCancel(DialogInterface dialog) {
                                finish();
                            }
                        }).show();
                break;
            //OK
            case ConnectionResult.SUCCESS:
                Log.d("TESTE", "Google play Services está atualizado!");
                break;
        }

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        //ULTIMA LOCALIZAÇÃO DO USUARIO
        client.getLastLocation()
                .addOnSuccessListener(new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {
                        if (location != null) {
                            Log.d("TESTE", location.getLatitude() + " " + location.getLongitude());
                        } else {
                            Log.d("TESTE", "Última localização não encontrada!");
                            //METODO QUE PEDE PARA O USUARIO ATIVAR O GPS
                            //solicitarAtivacaoGps();
                            controleGps();
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        });

        //MANIPULANDO COORDENADAS DE GPS

        //PEGANDO UMA INSTANCIA DO LOCATION REQUEST
        final LocationRequest locationRequest = LocationRequest.create();

        //QUERO QUE O APP BUSQUE A ATUALIZAÇÃO DO USUÁRIO A CADA 15 SEGUNDOS
        locationRequest.setInterval(15 * 1000);
        //SE OUTRO APP ESTIVER USANDO O RECURSO COMPARTILHADO, MEU APP VAI ATUALIZAR A CADA 5 SEGUNDOS, DO CONTRÁRIO, A CADA 15 SEGUNDOS
        locationRequest.setFastestInterval(5 * 1000);
        //BUSCA LOCALIZAÇÃO MAIS PRECISA DO USUARIO - IMPACTO (CONSUMO DE BATERIA)
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder()
                .addAllLocationRequests(Collections.singleton(locationRequest));

        //CONSTROI O LOCATION REQUEST E FAZ A VERIFICAÇÃO SE EXISTE ALGUM RECURSO ATIVO CAPAZ DE INFORMAR A LOCALIZAÇÃO
        SettingsClient settingsClient = LocationServices.getSettingsClient(this);
        settingsClient.checkLocationSettings(builder.build())
                .addOnSuccessListener(new OnSuccessListener<LocationSettingsResponse>() {
                    @Override
                    public void onSuccess(LocationSettingsResponse locationSettingsResponse) {
                        //REDE ATIVA
                        Log.i("TESTE", locationSettingsResponse.getLocationSettingsStates().isNetworkLocationPresent() + "");


                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                if (e instanceof ResolvableApiException) {
                    try {
                        ResolvableApiException resolvable = (ResolvableApiException) e;
                        resolvable.startResolutionForResult(PrincipalActivity.this, 10);
                    } catch (IntentSender.SendIntentException e1) {
                        e1.printStackTrace();
                    }
                }


            }
        });
        LocationCallback locationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                super.onLocationResult(locationResult);
                if (locationResult == null) {
                    Log.d("TESTE", "Localização indisponível!");
                    return;
                }
                //PROCURAR LOCALIZAÇÃO DISPONIVEL
                for (Location location : locationResult.getLocations()) {
                    Log.d("TESTE", "Latitude: " + location.getLatitude() + "");
                    Log.d("TESTE", "Longitude:" + location.getLongitude() + "");
                }
            }

            //DISPONIBILIDADE DA LOCALIZAÇÃO DO USUARIO
            @Override
            public void onLocationAvailability(LocationAvailability locationAvailability) {
                super.onLocationAvailability(locationAvailability);
                //LOCALIZAÇÃO ESTÁ DISPONIVEL?
                Log.d("TESTE", locationAvailability.isLocationAvailable() + "");
            }
        };

        client.requestLocationUpdates(locationRequest, locationCallback, null);
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

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
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
                .whereEqualTo("ESTADO", "RS")
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


    //PEDINDO PARA O USUARIO ATIVAR O GPS
    private void solicitarAtivacaoGps() {
        DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case DialogInterface.BUTTON_POSITIVE:
                        Intent callGPSSettingIntent = new Intent(
                                Settings.ACTION_LOCALE_SETTINGS);
                        startActivity(callGPSSettingIntent);
                        break;
                }
            }
        };

        android.app.AlertDialog.Builder alertDialog = new android.app.AlertDialog.Builder(this);
        //MENSAGEM PARA O USUARIO
        alertDialog.setMessage("Para utilizar o Kartel App você precisa permitir a ativação do GPS. Deseja ativa -lo ?");

        // ACTION PARA CONFIGURAR
        alertDialog.setPositiveButton("Configurar", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivity(intent);
            }
        });

        // ACTION DO BOTÃO CANCELAR
        alertDialog.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        alertDialog.show();
    }

    /**
     * ALERTAS DE CONFIGRAÇÃO DE GPS
     */
    public void controleGps() {
        android.app.AlertDialog.Builder alertDialog = new android.app.AlertDialog.Builder(this);
        alertDialog.setTitle("GPS");

        //MENSAGEM PARA O USUARIO
        alertDialog.setMessage("Para utilizar o Kartel App você precisa permitir a ativação do GPS. Deseja ativa -lo ?");

        // ACTION PARA CONFIGURAR
        alertDialog.setPositiveButton("Configurar", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivity(intent);
            }
        });

        // ACTION DO BOTÃO CANCELAR
        alertDialog.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        alertDialog.show();
    }

    public boolean buscarLocalizacao(Context context) {
        int REQUEST_PERMISSION_LOCALIZATION = 221;
        boolean res = true;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    public void requestPermissions(@NonNull String[] permissions, int requestCode)
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for Activity#requestPermissions for more details.

                res = false;
                ActivityCompat.requestPermissions((PrincipalActivity) context, new String[]{
                                Manifest.permission.ACCESS_FINE_LOCATION},
                        REQUEST_PERMISSION_LOCALIZATION);

            }
        }
        return res;
    }


    private String imprimirPostos() {
        String s = "";
        for (int i = 0; i < listaPostos.size(); i++) {
            s = s + listaPostos.get(i).toString() + "\n";
            Log.d("Posto:  ", listaPostos.get(i).getRazaoSocial()+" está na lista!");

        }
        return "Postos cadastrados  " + s;
    }

}
