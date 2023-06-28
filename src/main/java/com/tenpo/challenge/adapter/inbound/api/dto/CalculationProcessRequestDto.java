package com.tenpo.challenge.adapter.inbound.api.dto;

public record CalculationProcessRequestDto(Integer firstNumber, Integer secondNumber, String callbackUrl, String processType){}
