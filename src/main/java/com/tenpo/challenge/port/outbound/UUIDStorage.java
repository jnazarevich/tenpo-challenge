package com.tenpo.challenge.port.outbound;

import org.springframework.stereotype.Component;

import java.util.UUID;

public interface UUIDStorage {

    UUID randomUUID();

}
