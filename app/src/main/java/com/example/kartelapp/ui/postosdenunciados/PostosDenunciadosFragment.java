package com.example.kartelapp.ui.postosdenunciados;

import androidx.lifecycle.ViewModelProviders;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.kartelapp.Posto;
import com.example.kartelapp.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class PostosDenunciadosFragment extends Fragment {

    private PostosDenunciadosViewModel mViewModel;
    private static final String TAG = "TESTE";
    private ArrayList<Posto> postosDenunciados;
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    public static PostosDenunciadosFragment newInstance() {
        return new PostosDenunciadosFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.postos_denunciados_fragment, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = ViewModelProviders.of(this).get(PostosDenunciadosViewModel.class);
        // TODO: Use the ViewModel
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
                                Log.d(TAG, document.getId() + " => " + document.getData().toString());
                            }
                        } else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }
                    }
                });
        return denuncias;
    }
}
