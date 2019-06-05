package com.example.kartelapp;

import android.app.IntentService;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.os.ResultReceiver;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * CLASSE RESPONSÁVEL POR BUSCAR ENDEREÇOS BASEADOS EM DETERMINADAS LATITUDES
 */

public class FetchAddressService extends IntentService {

    protected ResultReceiver receiver;

    /**
     * Creates an IntentService.  Invoked by your subclass's constructor.
     */
    public FetchAddressService() {
        super("fetchAddressService");
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {

        //SE A INTENTAÃO FOR NULA VOU MATAR MEU MÉTODO
        if (intent == null) return;

        //REFERENCIA RESPONSÁVEL POR TRADUZIR LATITUDE E LONGITUDE
        Geocoder geocoder = new Geocoder(this, Locale.getDefault());

        //BUSCANDO LATITUDE E LONGITUDE DO OBJETO
        Location location = intent.getParcelableExtra(Constants.LOCATION_DATA_EXTRA);

        //DEVOLVENDO O ENDEREÇO NO FORMATO DE TEXTO
        receiver = intent.getParcelableExtra(Constants.RECEIVER);

        //REFERENCIA PARA UMA LISTA DE ENDEREÇOS
        List<Address> addresses = null;

        try {
            geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);

        } catch (IOException e) {
            Log.e("TESTE", "Serviço indisponível!", e);
        } catch (IllegalArgumentException e) {
            Log.e("TESTE", "Latitude ou logitude inválida!", e);
        }

        //TRATANDO CASO O GOOGLE NÃO CONSIGA IDENTIFICAR O ENDEREÇO
        if (addresses == null || addresses.isEmpty()) {
            Log.e("TESTE", "Nenhum endereço encontrado pelo Google!");
            deliverResultToReceiver(Constants.FAILURE_RESULT, "Nenhum endereço encontrado!");
        } else {
            //PEGANDO O PRIMEIRO ENDEREÇO DA LISTA DE ENDEREÇOS
            Address address = addresses.get(0);

            List<String> addressF = new ArrayList<>();

            //POPULANDO LISTA DE ENDEREÇOS
            for (int i = 0; i < address.getMaxAddressLineIndex(); i++) {
                addressF.add(address.getAddressLine(i));
            }
            //ENTREGANDO O RESULTADO DE SUCESSO PARA A PRINCIPAL ACTIVITY - TROCA DE MSG
            deliverResultToReceiver(Constants.SUCCES_RESULT, TextUtils.join("|", addressF));
        }
    }


    /**
     * MÉTODO RESPONSÁVEL POR PASSAR PARA A ACTIVITY  O RESULTADO RECEBIDO
     *
     * @param resultCode
     * @param message
     */
    private void deliverResultToReceiver(int resultCode, String message) {
        //PACOTE RESPONSAVEL POR PASSAR PARA A INTENÇÃO O RESULTADO OBTIDO
        Bundle bundle = new Bundle();
        bundle.putString(Constants.RESULT_DATA_KEY, message);
        receiver.send(resultCode, bundle);
    }
}
