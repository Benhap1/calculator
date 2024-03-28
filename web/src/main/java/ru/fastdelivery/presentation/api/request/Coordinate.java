package ru.fastdelivery.presentation.api.request;

import io.swagger.v3.oas.annotations.media.Schema;

public record Coordinate(
        @Schema(description = "Широта")
        Double latitude,
        @Schema(description = "Долгота")
        Double longitude
) {
}

