package com.tenpo.challenge.adapter.outbound.redis;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.redis.core.RedisHash;

@RedisHash(value = "percentage")
@Getter
@Setter
@AllArgsConstructor
@Builder
public class PercentageEntity {
    @Id
    @GeneratedValue
    private Long id;
    private String percentageValue;
    private String createdAt;
}
