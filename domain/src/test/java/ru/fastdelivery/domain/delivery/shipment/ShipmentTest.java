package ru.fastdelivery.domain.delivery.shipment;

import org.junit.jupiter.api.Test;
import ru.fastdelivery.domain.common.currency.CurrencyFactory;
import ru.fastdelivery.domain.common.volume.Volume;
import ru.fastdelivery.domain.common.weight.Weight;
import ru.fastdelivery.domain.delivery.pack.Pack;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class ShipmentTest {

    @Test
    void whenSummarizingWeightOfAllPackages_thenReturnSum() {
        var weight1 = new Weight(BigDecimal.TEN);
        var weight2 = new Weight(BigDecimal.ONE);

        var volume1 = new Volume(BigInteger.TEN, BigInteger.TEN, BigInteger.TEN); // Пример объема упаковки 1
        var volume2 = new Volume(BigInteger.ONE, BigInteger.ONE, BigInteger.ONE); // Пример объема упаковки 2

        var packages = List.of(new Pack(weight1, volume1), new Pack(weight2, volume2)); // Используем объем в создании Pack
        var shipment = new Shipment(packages, new CurrencyFactory(code -> true).create("RUB"));

        var massOfShipment = shipment.weightAllPackages();

        assertThat(massOfShipment.weightGrams()).isEqualByComparingTo(BigDecimal.valueOf(11));
    }
}

