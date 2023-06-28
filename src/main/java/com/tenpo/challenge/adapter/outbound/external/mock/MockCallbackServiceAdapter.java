package com.tenpo.challenge.adapter.outbound.external.mock;

import com.tenpo.challenge.domain.model.CalculationResult;
import com.tenpo.challenge.port.outbound.CallbackManager;
import org.springframework.stereotype.Component;

@Component
public class MockCallbackServiceAdapter implements CallbackManager {
    @Override
    public CalculationResult send(CalculationResult calculationResult) {
        return calculationResult;
    }
}
