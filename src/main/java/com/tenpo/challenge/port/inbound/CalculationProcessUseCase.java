package com.tenpo.challenge.port.inbound;

import com.tenpo.challenge.domain.model.CalculationProcessExecuted;
import com.tenpo.challenge.domain.model.Process;

import java.util.List;

public interface CalculationProcessUseCase {
    Boolean isAbleToProcess();

    Process store(String callbackUrl);

    CalculationProcessExecuted executeCalculationProcess(List<Integer> numbers, String referenceId);
}
