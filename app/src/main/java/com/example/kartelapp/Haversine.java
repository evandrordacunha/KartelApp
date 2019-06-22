package com.example.kartelapp;

/**
 * CLASSE QUE ARMAZENA  CÁLCULOS DE DISTÂNCIA ENTRE DOIS PONTOS UTILIZANDO A FÓRMULA DE HAVERSINE
 */

public class Haversine {

    private static final int EARTH_RADIUS = 6373; // RAIO TERRESTRE EM KM

    public static double distance(double startLat, double startLong,
                                  double endLat, double endLong) {

        double lat1  = Math.toRadians(startLat);
        double lon1 = Math.toRadians(startLong);

        double lat2  = Math.toRadians(endLat);
        double lon2 = Math.toRadians(endLong);

        double dLong = lon2 - lon1;
        double dLat = lat2 - lat1;

        double a = Math.pow(Math.sin(dLat / 2), 2) + Math.cos(lat1) * Math.cos(lat2) * Math.pow(Math.sin(dLong / 2), 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

        return EARTH_RADIUS * c;
    }

}
