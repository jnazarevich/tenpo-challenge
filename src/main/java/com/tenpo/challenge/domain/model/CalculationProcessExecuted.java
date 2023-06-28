package com.tenpo.challenge.domain.model;

import java.math.BigDecimal;

public record CalculationProcessExecuted(String referenceId, String status, BigDecimal result) {}
