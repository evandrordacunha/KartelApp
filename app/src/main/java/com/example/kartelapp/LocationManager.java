package com.example.kartelapp;

import java.util.ArrayList;

public interface LocationManager {

    void buscarLocalizacaoGps();

    void exibirAlertas();

    ArrayList encontrarPermissoesNecessarias(ArrayList<String> solicitacao);

    boolean verificaPermissao(String permissao);

    boolean validaPedidoPermissao();


}
