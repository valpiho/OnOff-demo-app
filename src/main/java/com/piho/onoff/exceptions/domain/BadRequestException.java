package com.piho.onoff.exceptions.domain;

public class BadRequestException extends RuntimeException{

    public BadRequestException(String message) {
        super(message);
    }
}
