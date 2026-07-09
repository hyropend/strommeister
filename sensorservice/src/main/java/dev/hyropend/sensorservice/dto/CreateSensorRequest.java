package dev.hyropend.sensorservice.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record CreateSensorRequest(
        @NotBlank String serialNumber,
        @NotBlank String location,
        @NotNull Long facilityId
){}
