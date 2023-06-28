package com.tenpo.challenge.domain.model;

import java.time.LocalDateTime;

public record ServiceCall(LocalDateTime createdAt, ServiceCallTypeEnum serviceCallTypeEnum) {
}
