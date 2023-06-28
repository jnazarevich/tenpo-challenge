package com.tenpo.challenge.domain.process;

import com.tenpo.challenge.domain.model.*;
import com.tenpo.challenge.domain.exception.PercentageNotRetrievedException;
import com.tenpo.challenge.domain.model.Process;
import com.tenpo.challenge.port.inbound.CalculationProcessUseCase;
import com.tenpo.challenge.port.outbound.*;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Clock;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;

public class CalculationProcessService implements CalculationProcessUseCase {

    private final PercentagesManager percentagesManager;

    private final ProcessStorage processStorage;

    private final UUIDStorage uuidStorage;

    private final CallbackManager callbackManager;

    private final PercentageStorage percentageStorage;

    private final ServiceCallStorage serviceCallStorage;

    private final Clock clock;

    public CalculationProcessService(
            final PercentagesManager percentagesManager,
            final ProcessStorage processStorage,
            final UUIDStorage uuidStorage,
            final CallbackManager callbackManager,
            final PercentageStorage percentageStorage,
            final ServiceCallStorage serviceCallStorage,
            final Clock clock
            ){
        this.percentagesManager = percentagesManager;
        this.processStorage = processStorage;
        this.uuidStorage = uuidStorage;
        this.callbackManager = callbackManager;
        this.percentageStorage = percentageStorage;
        this.clock = clock;
        this.serviceCallStorage = serviceCallStorage;
    }

    @Override
    public Boolean isAbleToProcess() {
        // Estos valores (1 y 3 minutos ) puede ser pasado como parámetro o puede ser buscado en una property con el fin de hacerlo configurable.
        LocalDateTime from = LocalDateTime.now(clock).minus(1L,ChronoUnit.MINUTES);
        LocalDateTime to = LocalDateTime.now(clock);
        return (this.serviceCallStorage.findByPeriodAndType(from,to, ServiceCallTypeEnum.INCOMING).size() <= 3);
    }

    @Override
    public Process store(String callbackUrl) {
        return this.processStorage.store(
            uuidStorage.randomUUID().toString(),
                ProcessStatusEnum.STARTED.name(),
                callbackUrl
        );
    }

    @Override
    public CalculationProcessExecuted executeCalculationProcess(List<Integer> numbers, String referenceId) {
        Optional<PercentageData> optionalPercentageData = this.percentageStorage.findLast();

        BigDecimal percentageValue = optionalPercentageData
                .filter(percentageData ->
                        !percentageData
                                .createdAt().isBefore(LocalDateTime.now(clock).minus(30L, ChronoUnit.MINUTES))
                )
                .orElseGet(() -> this.retrievePercentageData(optionalPercentageData))
                .percentageValue()
                .divide(BigDecimal.valueOf(100L), RoundingMode.HALF_EVEN).setScale(2,RoundingMode.HALF_EVEN);

        Integer sumReceived = numbers.stream().reduce(0, Integer::sum);
        BigDecimal result = percentageValue.add(BigDecimal.ONE).multiply(BigDecimal.valueOf(sumReceived));

        String status = ProcessStatusEnum.COMPLETED.name();

        // En caso de manejarlo de forma asincrónica, se recupera de la tabla process el callback_url buscando por referenceId, se agrega ese dato a esta llamada y se implementa un cliente genérico para poder consumir ese servicio.
        CalculationResult calculationResult = this.callbackManager.send(
                new CalculationResult(
                        referenceId,
                        status,
                        result
                )
        );

        return new CalculationProcessExecuted(
                referenceId,
                status,
                result
        );
    }

    private PercentageData retrievePercentageData(Optional<PercentageData> optionalPercentageData) {
        try {
            PercentageData percentageData = this.percentagesManager.retrieveData();
            this.percentageStorage.store(percentageData);
            return percentageData;
        } catch (PercentageNotRetrievedException e){
            return optionalPercentageData.orElseThrow(() -> new PercentageNotRetrievedException("Percentage data unavailable"));
        }
    }
}
