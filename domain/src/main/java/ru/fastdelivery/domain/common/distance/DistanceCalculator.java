package ru.fastdelivery.domain.common.distance;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class DistanceCalculator {

    @Value("${coordinates.latitude.min}")
    private double minLatitude;

    @Value("${coordinates.latitude.max}")
    private double maxLatitude;

    @Value("${coordinates.longitude.min}")
    private double minLongitude;

    @Value("${coordinates.longitude.max}")
    private double maxLongitude;

    public double calculateDistance(double lat1, double lon1, double lat2, double lon2) {
        // Проверяем, что координаты находятся в допустимых пределах
        validateCoordinates(lat1, lon1);
        validateCoordinates(lat2, lon2);

        // Рассчитываем расстояние между точками по формуле гаверсинусов
        double earthRadiusKm = 6372.795;

        double dLat = Math.toRadians(lat2 - lat1);
        double dLon = Math.toRadians(lon2 - lon1);
        double lat1Rad = Math.toRadians(lat1);
        double lat2Rad = Math.toRadians(lat2);

        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2) +
                Math.sin(dLon / 2) * Math.sin(dLon / 2) * Math.cos(lat1Rad) * Math.cos(lat2Rad);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

        return earthRadiusKm * c;
    }

    private void validateCoordinates(double latitude, double longitude) {
        if (latitude < minLatitude || latitude > maxLatitude ||
                longitude < minLongitude || longitude > maxLongitude) {
            throw new IllegalArgumentException("Invalid coordinates");
        }
    }
}
