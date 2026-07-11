package dev.hyropend.sensorservice.exception;

public abstract class DuplicateResourceException extends RuntimeException{
    protected DuplicateResourceException(String message){
        super(message);
    }
}
