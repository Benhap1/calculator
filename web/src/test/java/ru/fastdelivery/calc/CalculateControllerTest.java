package ru.fastdelivery.calc;

//import org.junit.jupiter.api.DisplayName;
//import org.junit.jupiter.api.Test;
//import org.mockito.Mockito;
//import org.springframework.boot.test.mock.mockito.MockBean;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import ru.fastdelivery.ControllerTest;
//import ru.fastdelivery.domain.common.price.Price;
//import ru.fastdelivery.presentation.api.request.CalculatePackagesRequest;
//import ru.fastdelivery.presentation.api.request.CargoPackage;
//import ru.fastdelivery.presentation.api.response.CalculatePackagesResponse;
//import ru.fastdelivery.usecase.TariffCalculateUseCase;
//
//import java.math.BigDecimal;
//import java.math.BigInteger;
//import java.util.List;
//
//import static org.junit.jupiter.api.Assertions.assertEquals;
//import static org.mockito.ArgumentMatchers.any;
//import static org.mockito.Mockito.when;
//
//class CalculateControllerTest extends ControllerTest {
//
//    final String baseCalculateApi = "/api/v1/calculate/";
//    @MockBean
//    TariffCalculateUseCase useCase;
//
//    @Test
//    @DisplayName("Валидные данные для расчета стоимости -> Ответ 200")
//    void whenValidInputData_thenReturn200() {
//        var request = new CalculatePackagesRequest(
//                List.of(new CargoPackage(BigDecimal.TEN, BigInteger.ONE, BigInteger.ONE, BigInteger.ONE)), "RUB",
//                null, null);
//        when(useCase.calc(any())).thenReturn(new Price(BigDecimal.valueOf(10), Mockito.any()));
//
//        ResponseEntity<CalculatePackagesResponse> response =
//                restTemplate.postForEntity(baseCalculateApi, request, CalculatePackagesResponse.class);
//
//        assertEquals(HttpStatus.OK, response.getStatusCode());
//    }
//
//    @Test
//    @DisplayName("Список упаковок == null -> Ответ 400")
//    void whenEmptyListPackages_thenReturn400() {
//        var request = new CalculatePackagesRequest(null, "RUB", null, null);
//
//        ResponseEntity<String> response = restTemplate.postForEntity(baseCalculateApi, request, String.class);
//
//        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
//    }
//}



import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
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
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

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
        MockitoAnnotations.initMocks(this);
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
        when(tariffCalculateUseCase.calc(any(Shipment.class), anyInt())).thenReturn(new Price(BigDecimal.valueOf(7302.40), new Currency("RUB")));

    }
}


