package com.tenpo.challenge.adapter.outbound.postgres;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;

import java.time.LocalDateTime;

@Entity(name = "service_call")
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
public class ServiceCallEntity {
    @Id
    @GeneratedValue
    private Long id;

    @Column(name = "http_method", nullable = false)
    private String httpMethod;

    @Column(name = "url", nullable = false)
    private String url;

    @Column(name = "headers", length = 5000)
    private String headers;

    @Column(name = "request", length = 5000)
    private String request;

    @Column(name = "response", length = 5000)
    private String response;

    @Column(name = "http_status")
    private String httpStatus;

    @Column(name = "type", nullable = false)
    private String type;

    @Column(name = "created_at", nullable = false)
    @CreatedDate
    private LocalDateTime createdAt;

}
