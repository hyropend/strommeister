package dev.hyropend.sensorservice.exception;

public class FacilityNotFoundException extends RuntimeException {
    public FacilityNotFoundException(Long id) {
        super("Facility with id " + id + " not found");
    }
}
