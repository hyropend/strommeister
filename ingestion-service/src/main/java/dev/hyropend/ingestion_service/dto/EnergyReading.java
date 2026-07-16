package dev.hyropend.ingestion_service.dto;

import java.time.Instant;

public record EnergyReading(
        String sensorId,
        double powerKw,
        Instant timestamp
) {
}
