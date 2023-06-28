package com.tenpo.challenge.domain.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record PercentageData(BigDecimal percentageValue, LocalDateTime createdAt) {
}
