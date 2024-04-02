package ru.fastdelivery.calc;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

import ru.fastdelivery.domain.common.currency.Currency;
import ru.fastdelivery.domain.common.currency.CurrencyFactory;
import ru.fastdelivery.domain.common.distance.DistanceCalculator;
import ru.fastdelivery.domain.common.price.Price;
import ru.fastdelivery.domain.common.volume.Volume;
import ru.fastdelivery.domain.common.weight.Weight;
import ru.fastdelivery.domain.delivery.pack.Pack;
import ru.fastdelivery.domain.delivery.shipment.Shipment;
import ru.fastdelivery.presentation.api.request.CalculatePackagesRequest;
import ru.fastdelivery.presentation.api.response.CalculatePackagesResponse;
import ru.fastdelivery.presentation.calc.CalculateController;
import ru.fastdelivery.usecase.TariffCalculateUseCase;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class CalculateControllerTest {

    @Mock
    private TariffCalculateUseCase tariffCalculateUseCase;

    @Mock
    private CurrencyFactory currencyFactory;

    @Mock
    private DistanceCalculator distanceCalculator;

    @InjectMocks
    private CalculateController calculateController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

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

        when(currencyFactory.create(anyString())).thenReturn(new Currency("RUB"));
        when(distanceCalculator.calculateDistance(anyDouble(), anyDouble(), anyDouble(), anyDouble())).thenReturn(1234.56);
        when(tariffCalculateUseCase.minimalPrice()).thenReturn(new Price(BigDecimal.valueOf(350), new Currency("RUB")));
        when(tariffCalculateUseCase.calc(eq(expectedShipment),anyDouble())).thenReturn(new Price(BigDecimal.valueOf(7302.4), new Currency("RUB")));

        // Act
        CalculatePackagesResponse response = calculateController.calculate(request);

        // Assert
        assertEquals(new BigDecimal("7302.4"),response.totalPrice());
        assertEquals(new BigDecimal("350"), response.minimalPrice());
        assertEquals("RUB", response.currencyCode());
    }
}
