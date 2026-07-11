package dev.hyropend.sensorservice.exception;

public class DuplicateSensorException extends DuplicateResourceException{
    public DuplicateSensorException(String serialNumber){
        super("Sensor with serial number '" + serialNumber + "' already exists");
    }
}
