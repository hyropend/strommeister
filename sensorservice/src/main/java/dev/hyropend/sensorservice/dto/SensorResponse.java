package dev.hyropend.sensorservice.dto;

import dev.hyropend.sensorservice.entity.SensorStatus;

import java.time.Instant;

public record SensorResponse(
        Long id,
        String serialNumber,
        String location,
        SensorStatus status,
        Instant installedAt,
        Long facilityId,
        String facilityName
){}
