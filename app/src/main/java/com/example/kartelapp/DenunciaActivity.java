package com.example.kartelapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.example.kartelapp.ui.postosdenunciados.PostosDenunciadosFragment;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class DenunciaActivity extends AppCompatActivity{

    private static final String TAG = "Documentos";
    private ArrayList<Posto> postosDenunciados;
    FirebaseFirestore db = FirebaseFirestore.getInstance();


    public DenunciaActivity(ArrayList<Posto> postosDenunciados, FirebaseFirestore db) {
        this.postosDenunciados = postosDenunciados;
        this.db = db;
    }

    public static String getTAG() {
        return TAG;
    }

    public ArrayList<Posto> getDenuncias() {
        return postosDenunciados;
    }

    public void setDenuncias(ArrayList<Posto> postosDenunciados) {
        this.postosDenunciados = postosDenunciados;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.postos_denunciados_activity);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.container, PostosDenunciadosFragment.newInstance())
                    .commitNow();
        }
        buscarPostosMaisDenunciados();
    }
    private ArrayList<String> buscarPostosMaisDenunciados() {
        final ArrayList<String> denuncias = new ArrayList<>();
        db.collection("denuncias")
                .whereGreaterThan("data", "")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                denuncias.add(document.getData().toString());
                                Log.d(TAG, document.getId() + " => " + document.getData());
                            }
                        } else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }
                    }
                });
        return denuncias;
    }
}
