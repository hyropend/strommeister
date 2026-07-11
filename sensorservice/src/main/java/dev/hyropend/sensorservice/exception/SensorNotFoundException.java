package dev.hyropend.sensorservice.exception;

public class SensorNotFoundException extends ResourceNotFoundException {
    public SensorNotFoundException(Long id) {
        super("Sensor with id " + id + " not found");
    }
}
