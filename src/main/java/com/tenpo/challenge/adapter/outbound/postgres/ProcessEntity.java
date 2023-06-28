package com.tenpo.challenge.adapter.outbound.postgres;

import jakarta.persistence.*;
import lombok.*;

@Entity(name = "process")
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
public class ProcessEntity {

    @Id
    @GeneratedValue
    @Column(name = "id", unique = true, nullable = false)
    private Long id;

    @Column(name = "status", nullable = false)
    private String status;

    @Column(name = "reference_id", nullable = false)
    private String referenceId;

    @Column(name = "callback_url")
    private String callbackUrl;
}
