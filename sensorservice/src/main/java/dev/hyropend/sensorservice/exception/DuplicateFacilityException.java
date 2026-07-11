package dev.hyropend.sensorservice.exception;

public class DuplicateFacilityException extends DuplicateResourceException {
    public DuplicateFacilityException(String name) {
        super("Facility with name '" + name + "' already exists");
    }
}
