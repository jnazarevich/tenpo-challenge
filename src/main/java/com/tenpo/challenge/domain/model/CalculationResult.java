package com.tenpo.challenge.domain.model;

import java.math.BigDecimal;

public record CalculationResult(String referenceId, String status, BigDecimal result) {
}
