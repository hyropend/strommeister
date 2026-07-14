package dev.hyropend.sensorsimulator.dto;

import java.time.Instant;

public record EnergyReading(
        String sensorId,
        double powerKw,
        Instant timestamp
) {
}
