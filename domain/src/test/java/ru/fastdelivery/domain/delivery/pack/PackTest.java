package ru.fastdelivery.domain.delivery.pack;

import org.junit.jupiter.api.Test;
import ru.fastdelivery.domain.common.volume.Volume;
import ru.fastdelivery.domain.common.weight.Weight;

import java.math.BigDecimal;
import java.math.BigInteger;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class PackTest {

    @Test
    void whenWeightMoreThanMaxWeight_thenThrowException() {
        var weight = new Weight(BigDecimal.valueOf(150_001));
        // Создаем заглушку для объема
        var volumeMock = mock(Volume.class);
        // Указываем фиктивные значения объема, которые не влияют на тест
        when(volumeMock.length()).thenReturn(BigInteger.valueOf(0));
        when(volumeMock.width()).thenReturn(BigInteger.valueOf(0));
        when(volumeMock.height()).thenReturn(BigInteger.valueOf(0));

        assertThatThrownBy(() -> new Pack(weight, volumeMock))
                .isInstanceOf(IllegalArgumentException.class);
    }


    @Test
    void whenWeightLessThanMaxWeight_thenObjectCreated() {
        var weight = new Weight(BigDecimal.valueOf(1_000));
        // Создаем заглушку для объема
        var volumeMock = mock(Volume.class);
        // Указываем фиктивные значения объема, которые не влияют на тест
        when(volumeMock.length()).thenReturn(BigInteger.valueOf(1));
        when(volumeMock.width()).thenReturn(BigInteger.valueOf(1));
        when(volumeMock.height()).thenReturn(BigInteger.valueOf(1));

        var actual = new Pack(weight, volumeMock);
        assertThat(actual.weight()).isEqualTo(new Weight(BigDecimal.valueOf(1_000)));
    }
}