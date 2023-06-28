package com.tenpo.challenge.adapter.inbound.api.exception;

public class InvalidAttributeException extends RuntimeException{
    public InvalidAttributeException(String message){super(message);}
    public InvalidAttributeException(String message, Throwable throwable){super(message,throwable);}
}
