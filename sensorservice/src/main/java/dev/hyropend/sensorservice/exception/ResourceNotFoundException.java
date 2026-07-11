package dev.hyropend.sensorservice.exception;

public abstract class ResourceNotFoundException extends RuntimeException{
    protected ResourceNotFoundException(String message){
        super(message);
    }
}
