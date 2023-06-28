package com.tenpo.challenge.adapter.inbound.api;

import com.tenpo.challenge.adapter.inbound.api.dto.CalculationProcessRequestDto;
import com.tenpo.challenge.adapter.inbound.api.dto.CalculationProcessResponseDto;
import com.tenpo.challenge.adapter.inbound.api.dto.ProcessTypeEnum;
import com.tenpo.challenge.adapter.inbound.api.exception.InvalidAttributeException;
import com.tenpo.challenge.adapter.inbound.api.exception.MaxRequestsNumberAchievedException;
import com.tenpo.challenge.adapter.inbound.api.mapper.CalculationProcessMapper;
import com.tenpo.challenge.domain.model.Process;
import com.tenpo.challenge.port.inbound.CalculationProcessUseCase;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.CompletableFuture;

@Service
public class CalculationProcessApiService {

    private final Logger logger = LoggerFactory.getLogger(CalculationProcessApiService.class);

    private final CalculationProcessUseCase calculationProcessUseCase;
    private final CalculationProcessMapper calculationProcessMapper;

    @Autowired
    public CalculationProcessApiService(
            final CalculationProcessUseCase calculationProcessUseCase,
            final CalculationProcessMapper calculationProcessMapper
            ) {
        this.calculationProcessUseCase = calculationProcessUseCase;
        this.calculationProcessMapper = calculationProcessMapper;
    }

    public CalculationProcessResponseDto executeCalculationProcess(
            final CalculationProcessRequestDto calculationProcessRequestDto
    ) {
        logger.info("Calculation process started");

        if(calculationProcessRequestDto.firstNumber() == null)
            throw new InvalidAttributeException("attribute 'firstNumber' is required");
        else if(calculationProcessRequestDto.secondNumber() == null)
            throw new InvalidAttributeException("attribute 'secondNumber' is required");

        // Valor por default
        ProcessTypeEnum processType = ProcessTypeEnum.SYNC;

        if(calculationProcessRequestDto.processType() != null) {
            if(!calculationProcessRequestDto.processType().equals(ProcessTypeEnum.SYNC.name()) && !calculationProcessRequestDto.processType().equals(ProcessTypeEnum.ASYNC.name()))
                throw new InvalidAttributeException("attribute 'type' must be SYNC or ASYNC" );
            else
                processType = ProcessTypeEnum.valueOf(calculationProcessRequestDto.processType());
        }

        if (calculationProcessUseCase.isAbleToProcess()){
            String callbackUrl = calculationProcessRequestDto.callbackUrl() != null ? calculationProcessRequestDto.callbackUrl() : null;
            Process processStored = this.calculationProcessUseCase.store(callbackUrl);

            if(processType.equals(ProcessTypeEnum.SYNC)){
                return this.calculationProcessMapper.modelToDto(
                        this.calculationProcessUseCase.executeCalculationProcess(
                                List.of(
                                        calculationProcessRequestDto.firstNumber(),
                                        calculationProcessRequestDto.secondNumber()
                                ),
                                processStored.referenceId()
                        )
                );
            } else {
                CompletableFuture.runAsync(() -> this.calculationProcessMapper.modelToDto(
                                this.calculationProcessUseCase.executeCalculationProcess(
                                        List.of(
                                                calculationProcessRequestDto.firstNumber(),
                                                calculationProcessRequestDto.secondNumber()
                                        ),
                                        processStored.referenceId()
                                )
                        )
                );
                return CalculationProcessResponseDto.builder()
                        .referenceId(processStored.referenceId())
                        .status(processStored.status())
                        .build();
            }
        } else {
            throw new MaxRequestsNumberAchievedException("Max requests number achieved");
        }


    }
}
