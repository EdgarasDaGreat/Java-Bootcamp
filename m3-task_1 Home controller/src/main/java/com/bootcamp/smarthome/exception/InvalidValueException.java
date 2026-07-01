package com.bootcamp.smarthome.exception;

public class InvalidValueException extends HomeAutomationException {

    public InvalidValueException(String field, Object value, String constraint){
        super(String.format("Invalid value '%s' for field '%s'. Must be in %s", value, field, constraint));
    }


    public InvalidValueException(String message) {
        super(message);
    }

}
