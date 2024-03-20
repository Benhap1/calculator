package ru.fastdelivery.domain.common.volume;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;

public record Volume(
        BigInteger length,
        BigInteger width,
        BigInteger height
) {
    public BigDecimal calculateVolume() {
        // Округляем параметры length, width и height до ближайшего кратного 50
        BigInteger roundedLength = roundToNearestMultiple(length, BigInteger.valueOf(50));
        BigInteger roundedWidth = roundToNearestMultiple(width, BigInteger.valueOf(50));
        BigInteger roundedHeight = roundToNearestMultiple(height, BigInteger.valueOf(50));

        // Вычисляем объем
        BigDecimal volume = new BigDecimal(roundedLength)
                .multiply(new BigDecimal(roundedWidth))
                .multiply(new BigDecimal(roundedHeight))
                .divide(BigDecimal.valueOf(1_000_000_000), 4, RoundingMode.HALF_UP);

        return volume;
    }

    private BigInteger roundToNearestMultiple(BigInteger value, BigInteger multiple) {
        BigInteger remainder = value.mod(multiple);
        if (remainder.compareTo(multiple.divide(BigInteger.valueOf(2))) >= 0) {
            return value.add(multiple.subtract(remainder));
        } else {
            return value.subtract(remainder);
        }
    }
}