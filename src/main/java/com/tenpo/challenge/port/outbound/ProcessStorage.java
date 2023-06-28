package com.tenpo.challenge.port.outbound;

import com.tenpo.challenge.domain.model.Process;

public interface ProcessStorage {
    Process store(String referenceId, String status, String callbackUrl);
}
