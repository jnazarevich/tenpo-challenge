package com.tenpo.challenge.adapter.outbound.uuid;

import com.tenpo.challenge.port.outbound.UUIDStorage;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class UUIDAdapter implements UUIDStorage {
    @Override
    public UUID randomUUID() {
        return UUID.randomUUID();
    }
}
