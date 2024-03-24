package ru.fastdelivery.usecase;

import lombok.RequiredArgsConstructor;
import ru.fastdelivery.domain.common.price.Price;
import ru.fastdelivery.domain.delivery.shipment.Shipment;

import javax.inject.Named;
import java.math.BigDecimal;

@Named
@RequiredArgsConstructor
public class TariffCalculateUseCase {
    private final WeightPriceProvider weightPriceProvider;
    private final VolumePriceProvider volumePriceProvider;

    public Price calc(Shipment shipment) {
        BigDecimal minimalPrice = weightPriceProvider.minimalPrice().amount();

        BigDecimal totalVolume = shipment.packages().stream()
                .map(pack -> pack.volume().calculateVolume())
                .reduce(BigDecimal.ZERO, BigDecimal::add); // Суммируем объемы всех упаковок

        BigDecimal volumePrice = totalVolume.multiply(volumePriceProvider.costPerCubicMeter().amount());

        BigDecimal weightPrice = shipment.weightAllPackages().kilograms().multiply(weightPriceProvider.costPerKg().amount());

        BigDecimal totalPrice = volumePrice.max(weightPrice).max(minimalPrice);

        return new Price(totalPrice, weightPriceProvider.costPerKg().currency());
    }

    public Price minimalPrice() {
        return weightPriceProvider.minimalPrice();
    }
}



