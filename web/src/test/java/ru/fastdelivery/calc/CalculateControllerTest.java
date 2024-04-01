package ru.fastdelivery.calc;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.fastdelivery.domain.common.currency.Currency;
import ru.fastdelivery.domain.common.currency.CurrencyFactory;
import ru.fastdelivery.domain.common.distance.DistanceCalculator;
import ru.fastdelivery.domain.common.volume.Volume;
import ru.fastdelivery.domain.common.weight.Weight;
import ru.fastdelivery.domain.delivery.pack.Pack;
import ru.fastdelivery.domain.delivery.shipment.Shipment;
import ru.fastdelivery.presentation.api.request.CalculatePackagesRequest;
import ru.fastdelivery.presentation.calc.CalculateController;
import ru.fastdelivery.usecase.TariffCalculateUseCase;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.List;

@ExtendWith(MockitoExtension.class)
class CalculateControllerTest {

    @Mock
    private TariffCalculateUseCase tariffCalculateUseCase;

    @Mock
    private CurrencyFactory currencyFactory;

    @Mock
    private DistanceCalculator distanceCalculator;

    @InjectMocks
    private CalculateController calculateController;

    @Test
    void testCalculatePackages() {
        // Arrange
        CalculatePackagesRequest request = new CalculatePackagesRequest(
                List.of(new ru.fastdelivery.presentation.api.request.CargoPackage(BigDecimal.valueOf(4564), BigInteger.valueOf(345), BigInteger.valueOf(589), BigInteger.valueOf(234))),
                "RUB",
                new ru.fastdelivery.presentation.api.request.Coordinate(46.398660, 55.027532),
                new ru.fastdelivery.presentation.api.request.Coordinate(55.446008, 65.339151)
        );

        Currency currency = new Currency("RUB");
        List<Pack> packs = List.of(new Pack(new Weight(BigDecimal.valueOf(4564)), new Volume(BigInteger.valueOf(345), BigInteger.valueOf(589), BigInteger.valueOf(234))));
        Shipment expectedShipment = new Shipment(packs, currency);

    }
}
