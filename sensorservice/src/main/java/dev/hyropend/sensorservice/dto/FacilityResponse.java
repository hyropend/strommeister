package dev.hyropend.sensorservice.dto;

import java.time.Instant;

public record FacilityResponse(
        Long id,
        String name,
        String address,
        Instant createdAt
) {
}
