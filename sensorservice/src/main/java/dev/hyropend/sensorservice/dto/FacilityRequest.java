package dev.hyropend.sensorservice.dto;

import jakarta.validation.constraints.NotBlank;


public record FacilityRequest(
        @NotBlank String name,
        @NotBlank String address
) {
}
