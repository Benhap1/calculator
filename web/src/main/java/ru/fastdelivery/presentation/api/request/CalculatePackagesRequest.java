package ru.fastdelivery.presentation.api.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.util.List;

@Schema(description = "Данные для расчета стоимости доставки")
public record CalculatePackagesRequest(
        @Schema(description = "Список упаковок отправления",
                example = "[{\"weight\": 4056.45,\"length\": 345,\"width\": 589,\"height\": 234} ]")
        @NotNull
        @NotEmpty
        List<CargoPackage> packages,

        @Schema(description = "Трехбуквенный код валюты", example = "RUB")
        @NotNull
        String currencyCode,

        @Schema(description = "Координаты пункта назначения",
                example = "{\"latitude\": 45,\"longitude\": 80}")
        @NotNull Coordinate destination,

        @Schema(description = "Координаты пункта отправления",
                example = "{\"latitude\": 48,\"longitude\": 85}")

        @NotNull
        Coordinate departure
) {
}
