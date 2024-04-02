package ru.fastdelivery.usecase;

import lombok.RequiredArgsConstructor;
import ru.fastdelivery.domain.common.price.Price;
import ru.fastdelivery.domain.delivery.shipment.Shipment;

import javax.inject.Named;
import java.math.BigDecimal;
import java.math.RoundingMode;

@Named
@RequiredArgsConstructor
public class TariffCalculateUseCase {
    private final WeightPriceProvider weightPriceProvider;
    private final VolumePriceProvider volumePriceProvider;

    public Price calc(Shipment shipment, double distance) {
        BigDecimal minimalPrice = weightPriceProvider.minimalPrice().amount();

        BigDecimal totalVolume = shipment.packages().stream()
                .map(pack -> pack.volume().calculateVolume())
                .reduce(BigDecimal.ZERO, BigDecimal::add); // Суммируем объемы всех упаковок

        BigDecimal volumePrice = totalVolume.multiply(volumePriceProvider.costPerCubicMeter().amount());

        BigDecimal weightPrice = shipment.weightAllPackages().kilograms().multiply(weightPriceProvider.costPerKg().amount());

        BigDecimal basePrice = volumePrice.max(weightPrice).max(minimalPrice);

        // Проверяем условие на расстояние и рассчитываем totalPrice соответственно
        BigDecimal totalPrice;
        if (distance <= 450) {
            // Если расстояние меньше или равно 450 км, используем базовую стоимость
            totalPrice = basePrice.setScale(2, RoundingMode.HALF_UP);
        } else {
            // Если расстояние больше 450 км, добавляем дополнительную стоимость за расстояние
            totalPrice = calculateAdditionalPrice(distance).setScale(2, RoundingMode.HALF_UP);;

        }

        return new Price(totalPrice, weightPriceProvider.costPerKg().currency());
    }

    public Price minimalPrice() {
        return weightPriceProvider.minimalPrice();
    }

    private BigDecimal calculateAdditionalPrice(double distance) {
        // Рассчитываем количество блоков по 450 км
        double blocks = distance / 450;
        // Общая стоимость за дополнительное расстояние
        return weightPriceProvider.minimalPrice().amount().multiply(BigDecimal.valueOf(blocks));

    }
}

