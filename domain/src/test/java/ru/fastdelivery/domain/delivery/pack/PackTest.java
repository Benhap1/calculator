package ru.fastdelivery.domain.delivery.pack;

import org.junit.jupiter.api.Test;
import ru.fastdelivery.domain.common.weight.Weight;
import ru.fastdelivery.domain.common.volume.Volume;

import java.math.BigDecimal;
import java.math.BigInteger;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class PackTest {

    @Test
    void whenWeightMoreThanMaxWeight_thenThrowException() {
        var weight = new Weight(BigDecimal.valueOf(150_001));
        assertThatThrownBy(() -> new Pack(weight, null)) // Передаем null вместо Volume, так как в данном тесте проверяется только вес
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void whenWeightLessThanMaxWeight_thenObjectCreated() {
        var weight = new Weight(BigDecimal.valueOf(1_000));
        var volume = new Volume(BigInteger.valueOf(100), BigInteger.valueOf(100), BigInteger.valueOf(100)); // Пример значений объема
        var actual = new Pack(weight, volume);
        assertThat(actual.weight()).isEqualTo(weight);
        assertThat(actual.volume()).isEqualTo(volume);
    }

    @Test
    void whenVolumeMoreThanMaxVolume_thenThrowException() {
        var weight = new Weight(BigDecimal.valueOf(100_000)); // Пример значения веса
        var volume = new Volume(BigInteger.valueOf(1501), BigInteger.valueOf(1500), BigInteger.valueOf(1500)); // Значения объема больше максимальных
        assertThatThrownBy(() -> new Pack(weight, volume))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void whenVolumeLessThanMaxVolume_thenObjectCreated() {
        var weight = new Weight(BigDecimal.valueOf(100_000)); // Пример значения веса
        var volume = new Volume(BigInteger.valueOf(1000), BigInteger.valueOf(1000), BigInteger.valueOf(1000)); // Значения объема в пределах допустимых
        var actual = new Pack(weight, volume);
        assertThat(actual.volume()).isEqualTo(volume);
    }
}
