package dev.hyropend.sensorservice.exception;

public class DuplicateFacilityException extends RuntimeException {
    public DuplicateFacilityException(String name) {
        super("Facility with that name '" + name + "' already exists");
    }
}
