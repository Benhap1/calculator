package ru.fastdelivery.properties.provider;

import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import ru.fastdelivery.domain.common.currency.CurrencyFactory;
import ru.fastdelivery.domain.common.price.Price;
import ru.fastdelivery.usecase.VolumePriceProvider;
import ru.fastdelivery.usecase.WeightPriceProvider;
import java.math.BigDecimal;

@ConfigurationProperties("cost.rub")
@Setter
public class PricesRublesProperties implements WeightPriceProvider, VolumePriceProvider {

    private BigDecimal perKg;
    private BigDecimal perCubicMeter;
    private BigDecimal minimal;

    @Autowired
    private CurrencyFactory currencyFactory;

    @Override
    public Price costPerKg() {
        return new Price(perKg, currencyFactory.create("RUB"));
    }

    @Override
    public Price minimalPrice() {
        return new Price(minimal, currencyFactory.create("RUB"));
    }

    @Override
    public Price costPerCubicMeter() {
        return new Price(perCubicMeter, currencyFactory.create("RUB"));
    }

}
