package ru.fastdelivery.domain.delivery.pack;

import ru.fastdelivery.domain.common.volume.Volume;
import ru.fastdelivery.domain.common.weight.Weight;

import java.math.BigInteger;

public record Pack(Weight weight, Volume volume) {
    // Определяем максимальные габариты и вес упаковки
    private static final Volume maxVolume = new Volume(BigInteger.valueOf(1500), BigInteger.valueOf(1500), BigInteger.valueOf(1500));
    private static final Weight maxWeight = new Weight(BigInteger.valueOf(150_000)); // Указываем в граммах

    public Pack {
        // Проверяем соответствие параметров упаковки ограничениям
        if (weight.greaterThan(maxWeight) ||
                volume.length().compareTo(BigInteger.ZERO) <= 0 ||
                volume.width().compareTo(BigInteger.ZERO) <= 0 ||
                volume.height().compareTo(BigInteger.ZERO) <= 0 ||
                volume.length().compareTo(maxVolume.length()) > 0 ||
                volume.width().compareTo(maxVolume.width()) > 0 ||
                volume.height().compareTo(maxVolume.height()) > 0) {
            throw new IllegalArgumentException("Invalid package parameters");
        }
    }
}
