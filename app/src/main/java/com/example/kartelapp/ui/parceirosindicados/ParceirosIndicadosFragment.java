package com.example.kartelapp.ui.parceirosindicados;

import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.kartelapp.R;

public class ParceirosIndicadosFragment extends Fragment {

    private ParceirosIndicadosViewModel mViewModel;

    public static ParceirosIndicadosFragment newInstance() {
        return new ParceirosIndicadosFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.parceiros_indicados_fragment, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = ViewModelProviders.of(this).get(ParceirosIndicadosViewModel.class);
        // TODO: Use the ViewModel
    }

}
