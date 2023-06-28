package com.tenpo.challenge.adapter.inbound.api.exception;

public class MaxRequestsNumberAchievedException extends RuntimeException{
    public MaxRequestsNumberAchievedException(String message){super(message);}

    public MaxRequestsNumberAchievedException(String message, Throwable throwable){super(message,throwable);}
}
