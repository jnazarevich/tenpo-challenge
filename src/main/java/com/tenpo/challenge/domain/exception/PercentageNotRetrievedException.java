package com.tenpo.challenge.domain.exception;

public class PercentageNotRetrievedException extends RuntimeException{
    public PercentageNotRetrievedException(String message){super(message);}
    public PercentageNotRetrievedException(String message, Throwable throwable){super(message,throwable);}
}
