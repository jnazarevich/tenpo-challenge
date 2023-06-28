package com.tenpo.challenge.adapter.inbound.api.dto;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class ErrorResponseDto {
    private String description;
    private String timestamp;
}
