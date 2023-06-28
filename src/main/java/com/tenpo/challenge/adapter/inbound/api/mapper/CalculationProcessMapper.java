package com.tenpo.challenge.adapter.inbound.api.mapper;

import com.tenpo.challenge.adapter.inbound.api.dto.CalculationProcessResponseDto;
import com.tenpo.challenge.domain.model.CalculationProcessExecuted;
import org.springframework.stereotype.Component;

@Component
public class CalculationProcessMapper {

    public CalculationProcessResponseDto modelToDto(CalculationProcessExecuted calculationProcessExecuted) {
        return new CalculationProcessResponseDto(
                calculationProcessExecuted.referenceId(),
                calculationProcessExecuted.status(),
                calculationProcessExecuted.result()
        );
    }

}
