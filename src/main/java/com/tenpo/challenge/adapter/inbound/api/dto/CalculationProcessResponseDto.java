package com.tenpo.challenge.adapter.inbound.api.dto;

import lombok.Builder;

import java.math.BigDecimal;

@Builder
public record CalculationProcessResponseDto(String referenceId, String status, BigDecimal result) { }
