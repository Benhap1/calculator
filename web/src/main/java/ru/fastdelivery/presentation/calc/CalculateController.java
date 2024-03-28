package ru.fastdelivery.presentation.calc;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.fastdelivery.domain.common.currency.CurrencyFactory;
import ru.fastdelivery.domain.common.distance.DistanceCalculator;
import ru.fastdelivery.domain.common.volume.Volume;
import ru.fastdelivery.domain.common.weight.Weight;
import ru.fastdelivery.domain.delivery.pack.Pack;
import ru.fastdelivery.domain.delivery.shipment.Shipment;
import ru.fastdelivery.presentation.api.request.CalculatePackagesRequest;
import ru.fastdelivery.presentation.api.response.CalculatePackagesResponse;
import ru.fastdelivery.usecase.TariffCalculateUseCase;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.HttpURLConnection;
import java.net.URL;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

@RestController
@RequestMapping("/api/v1/calculate/")
@RequiredArgsConstructor
@Tag(name = "Расчеты стоимости доставки")
public class CalculateController {
    private final TariffCalculateUseCase tariffCalculateUseCase;
    private final CurrencyFactory currencyFactory;
    private final DistanceCalculator distanceCalculator;

    @PostMapping
    @Operation(summary = "Расчет стоимости по упаковкам груза")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful operation"),
            @ApiResponse(responseCode = "400", description = "Invalid input provided")
    })
    public CalculatePackagesResponse calculate(@Valid @RequestBody CalculatePackagesRequest request) {
        try {
            // Узнаем текущий курс доллара
            double dollarRate = getDollarRate();

            // Проверяем, что вес упаковки не равен нулю
            if (request.packages().stream().anyMatch(pack -> pack.weight().equals(BigDecimal.ZERO))) {
                throw new IllegalArgumentException("Weight of the package cannot be zero");
            }

            // Создаем список упаковок для отправки
            List<Pack> packs = request.packages().stream()
                    .map(pack -> new Pack(new Weight(pack.weight()), new Volume(pack.length(), pack.width(), pack.height())))
                    .toList();

            // Рассчитываем расстояние между точками отправления и назначения
            double distance = distanceCalculator.calculateDistance(
                    request.departure().latitude(),
                    request.departure().longitude(),
                    request.destination().latitude(),
                    request.destination().longitude()
            );

            // Рассчитываем стоимость на основе веса и объема
            Shipment shipment = new Shipment(packs, currencyFactory.create(request.currencyCode()));
            BigDecimal basePrice = tariffCalculateUseCase.calc(shipment).amount();

            // Устанавливаем стоимость за каждые 450 км как базовую стоимость
            BigDecimal basePricePer450km = basePrice;

            // Рассчитываем количество блоков по 450 км
            int blocks = (int) Math.ceil(distance / 450);

            // Общая стоимость за дополнительное расстояние
            BigDecimal additionalPrice = basePricePer450km.multiply(BigDecimal.valueOf(blocks));

            // Итоговая стоимость
            BigDecimal totalPrice;
            if (request.currencyCode().equals("USD")) {
                // Если валюта USD, то переводим стоимость в USD по текущему курсу
                totalPrice = additionalPrice.divide(BigDecimal.valueOf(dollarRate), 2, RoundingMode.HALF_UP);
            } else {
                totalPrice = basePrice.add(additionalPrice);
            }

            return new CalculatePackagesResponse(totalPrice, tariffCalculateUseCase.minimalPrice().amount(), request.currencyCode());
        } catch (IOException | ParserConfigurationException | SAXException e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to get dollar rate");
        }
    }

    // Метод, который парсит курс доллара (XML-файл) с сайта ЦБР
    private static double getDollarRate() throws IOException, ParserConfigurationException, SAXException {
        LocalDate currentDate = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        String formattedDate = currentDate.format(formatter);

        URL url = new URL("http://www.cbr.ru/scripts/XML_daily.asp?date_req=" + formattedDate);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");

        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document doc = builder.parse(connection.getInputStream());

        NodeList nodeList = doc.getElementsByTagName("Valute");
        for (int i = 0; i < nodeList.getLength(); i++) {
            Element element = (Element) nodeList.item(i);
            String charCode = element.getElementsByTagName("CharCode").item(0).getTextContent();
            if (charCode.equals("USD")) {
                String valueStr = element.getElementsByTagName("Value").item(0).getTextContent().replace(",", ".");
                return Double.parseDouble(valueStr);
            }
        }
        return -1;
    }
}



