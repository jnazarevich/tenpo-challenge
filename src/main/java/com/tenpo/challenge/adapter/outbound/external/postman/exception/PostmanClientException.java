package com.tenpo.challenge.adapter.outbound.external.postman.exception;

public class PostmanClientException extends RuntimeException{

    public PostmanClientException(String message){ super(message);}

    public PostmanClientException(String message, Throwable throwable){super(message,throwable);}
}
