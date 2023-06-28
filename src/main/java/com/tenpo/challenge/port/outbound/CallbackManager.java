package com.tenpo.challenge.port.outbound;

import com.tenpo.challenge.domain.model.CalculationResult;

public interface CallbackManager {

    CalculationResult send(CalculationResult calculationResult);
}
