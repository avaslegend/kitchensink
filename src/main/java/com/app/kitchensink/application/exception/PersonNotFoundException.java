package com.app.kitchensink.application.exception;

public class PersonNotFoundException extends RuntimeException {
    
    public PersonNotFoundException(String id) {
        super(id);
    }
}