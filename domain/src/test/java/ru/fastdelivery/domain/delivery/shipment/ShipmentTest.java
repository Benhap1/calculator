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
        var volume1 = new Volume(BigInteger.valueOf(10), BigInteger.valueOf(10),BigInteger.valueOf(10));
        var weight2 = new Weight(BigDecimal.ONE);
        var volume2 = new Volume(BigInteger.valueOf(10), BigInteger.valueOf(10),BigInteger.valueOf(10));

        var packages = List.of(new Pack(weight1, volume1), new Pack(weight2, volume2));
        var shipment = new Shipment(packages, new CurrencyFactory(code -> true).create("RUB"));

        var massOfShipment = shipment.weightAllPackages();

        assertThat(massOfShipment.weightGrams()).isEqualByComparingTo(String.valueOf(BigInteger.valueOf(11)));

    }
}
