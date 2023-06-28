package com.tenpo.challenge.domain.model;

import java.time.LocalDateTime;

public record ServiceCallPageable(String httpMethod, String url, String headers, String request, String response, String httpStatus, String type, LocalDateTime createdAt) {
}
