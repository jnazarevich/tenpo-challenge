package com.tenpo.challenge.domain.process;

import com.tenpo.challenge.domain.exception.PercentageNotRetrievedException;
import com.tenpo.challenge.domain.model.CalculationProcessExecuted;
import com.tenpo.challenge.domain.model.PercentageData;
import com.tenpo.challenge.domain.model.ProcessStatusEnum;
import com.tenpo.challenge.port.outbound.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Clock;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;

public class CalculationProcessServiceTest {
    private final PercentageStorage percentageStorageMock = Mockito.mock(PercentageStorage.class);
    private final PercentagesManager percentagesManagerMock = Mockito.mock(PercentagesManager.class);
    private final ProcessStorage processStorageMock = Mockito.mock(ProcessStorage.class);
    private final UUIDStorage uuidStorageMock = Mockito.mock(UUIDStorage.class);
    private final CallbackManager callbackManagerMock = Mockito.mock(CallbackManager.class);
    private final ServiceCallStorage serviceCallStorageMock = Mockito.mock(ServiceCallStorage.class);

    private final Clock fixedClock = Clock.fixed(LocalDateTime.now().toInstant(ZoneOffset.UTC), ZoneId.from(ZoneOffset.UTC));

    @Test
    public void executeCalculationProcess_withPercentageValueStoredLessThan30minutesAgo_shouldReturnExpectedResultAndStatusCompleted(){

        BigDecimal percentageValue = BigDecimal.TEN.setScale(2, RoundingMode.HALF_EVEN);
        LocalDateTime createdAt = LocalDateTime.now(fixedClock).minus(1, ChronoUnit.MINUTES);

        Optional<PercentageData> percentageData = Optional.of
                (new PercentageData(
                                percentageValue, createdAt
                        )
                );

        Mockito.when(percentageStorageMock.findLast()).thenReturn(percentageData);

        CalculationProcessService calculationProcessService = new CalculationProcessService(
                percentagesManagerMock,
                processStorageMock,
                uuidStorageMock,
                callbackManagerMock,
                percentageStorageMock,
                serviceCallStorageMock,
                fixedClock
        );

        List<Integer> numbers = List.of(9, 6);
        String referenceId = "123";

        CalculationProcessExecuted calculationProcessExecuted = calculationProcessService.executeCalculationProcess(numbers, referenceId);

        BigDecimal resultExpected = new BigDecimal("16.5").setScale(2,RoundingMode.HALF_EVEN);

        Assertions.assertEquals(resultExpected,calculationProcessExecuted.result());
        Assertions.assertEquals(ProcessStatusEnum.COMPLETED.name(),calculationProcessExecuted.status());
    }

    @Test
    public void executeCalculationProcess_withPercentageValueStoredMoreThan30minutesAgoThenRetrievePercentageValueFromPercentageManager_shouldReturnExpectedResultAndStatusCompleted(){
        BigDecimal percentageValueFromStorage = BigDecimal.TEN.setScale(2, RoundingMode.HALF_EVEN);
        LocalDateTime createdAtFromStorage = LocalDateTime.now(fixedClock).minus(31, ChronoUnit.MINUTES);

        Optional<PercentageData> storedPercentageData = Optional.of
                (new PercentageData(percentageValueFromStorage, createdAtFromStorage));

        BigDecimal percentaValueFromProvider = BigDecimal.valueOf(50).setScale(2, RoundingMode.HALF_EVEN);
        LocalDateTime createdAtFromProvider = LocalDateTime.now(fixedClock);

        PercentageData percentageDataFromProvider = new PercentageData(
                percentaValueFromProvider,createdAtFromProvider
        );

        Mockito.when(percentageStorageMock.findLast()).thenReturn(storedPercentageData);
        Mockito.when(percentageStorageMock.store(percentageDataFromProvider)).thenReturn(percentageDataFromProvider);
        Mockito.when(percentagesManagerMock.retrieveData()).thenReturn(percentageDataFromProvider);

        CalculationProcessService calculationProcessService = new CalculationProcessService(
                percentagesManagerMock,
                processStorageMock,
                uuidStorageMock,
                callbackManagerMock,
                percentageStorageMock,
                serviceCallStorageMock,
                fixedClock
        );

        List<Integer> numbers = List.of(9, 6);
        String referenceId = "123";

        CalculationProcessExecuted calculationProcessExecuted = calculationProcessService.executeCalculationProcess(numbers, referenceId);

        BigDecimal resultExpected = new BigDecimal("22.5").setScale(2,RoundingMode.HALF_EVEN);

        Assertions.assertEquals(resultExpected,calculationProcessExecuted.result());
        Assertions.assertEquals(ProcessStatusEnum.COMPLETED.name(),calculationProcessExecuted.status());
    }

    @Test
    public void executeCalculationProcess_withoutPercentageValueStoredThenRetrievePercentageValueFromPercentageManager_shouldReturnExpectedResultAndStatusCompleted(){
        Optional<PercentageData> storedPercentageData = Optional.empty();

        BigDecimal percentaValueFromProvider = BigDecimal.valueOf(50).setScale(2, RoundingMode.HALF_EVEN);
        LocalDateTime createdAtFromProvider = LocalDateTime.now(fixedClock);

        PercentageData percentageDataFromProvider = new PercentageData(
                percentaValueFromProvider,createdAtFromProvider
        );

        Mockito.when(percentageStorageMock.findLast()).thenReturn(storedPercentageData);
        Mockito.when(percentageStorageMock.store(percentageDataFromProvider)).thenReturn(percentageDataFromProvider);
        Mockito.when(percentagesManagerMock.retrieveData()).thenReturn(percentageDataFromProvider);

        CalculationProcessService calculationProcessService = new CalculationProcessService(
                percentagesManagerMock,
                processStorageMock,
                uuidStorageMock,
                callbackManagerMock,
                percentageStorageMock,
                serviceCallStorageMock,
                fixedClock
        );

        List<Integer> numbers = List.of(9, 6);
        String referenceId = "123";

        CalculationProcessExecuted calculationProcessExecuted = calculationProcessService.executeCalculationProcess(numbers, referenceId);

        BigDecimal resultExpected = new BigDecimal("22.5").setScale(2,RoundingMode.HALF_EVEN);

        Assertions.assertEquals(resultExpected,calculationProcessExecuted.result());
        Assertions.assertEquals(ProcessStatusEnum.COMPLETED.name(),calculationProcessExecuted.status());
    }

    @Test
    public void executeCalculationProcess_withPercentageValueStoredMoreThan30minutesAgoThenRetrievePercentageValueFromPercentageManagerThrowsError_shouldReturnExpectedResultAndStatusCompleted(){
        BigDecimal percentageValueFromStorage = BigDecimal.TEN.setScale(2, RoundingMode.HALF_EVEN);
        LocalDateTime createdAtFromStorage = LocalDateTime.now(fixedClock).minus(31, ChronoUnit.MINUTES);

        Optional<PercentageData> storedPercentageData = Optional.of
                (new PercentageData(percentageValueFromStorage, createdAtFromStorage));

        Mockito.when(percentageStorageMock.findLast()).thenReturn(storedPercentageData);
        Mockito.when(percentagesManagerMock.retrieveData()).thenThrow(new PercentageNotRetrievedException("Percentage data unavailable"));

        CalculationProcessService calculationProcessService = new CalculationProcessService(
                percentagesManagerMock,
                processStorageMock,
                uuidStorageMock,
                callbackManagerMock,
                percentageStorageMock,
                serviceCallStorageMock,
                fixedClock
        );

        List<Integer> numbers = List.of(9, 6);
        String referenceId = "123";

        CalculationProcessExecuted calculationProcessExecuted = calculationProcessService.executeCalculationProcess(numbers, referenceId);

        BigDecimal resultExpected = new BigDecimal("16.5").setScale(2,RoundingMode.HALF_EVEN);

        Assertions.assertEquals(resultExpected,calculationProcessExecuted.result());
        Assertions.assertEquals(ProcessStatusEnum.COMPLETED.name(),calculationProcessExecuted.status());
    }

    @Test
    public void executeCalculationProcess_withoutPercentageValueStoredThenRetrievePercentageValueFromPercentageManagerThrowsError_shouldThrowsPercentageNotRetrievedException(){
        Assertions.assertThrows(PercentageNotRetrievedException.class, () -> {
                    Optional<PercentageData> storedPercentageData = Optional.empty();

                    Mockito.when(percentageStorageMock.findLast()).thenReturn(storedPercentageData);
                    Mockito.when(percentagesManagerMock.retrieveData()).thenThrow(new PercentageNotRetrievedException("Percentage data unavailable"));

                    CalculationProcessService calculationProcessService = new CalculationProcessService(
                            percentagesManagerMock,
                            processStorageMock,
                            uuidStorageMock,
                            callbackManagerMock,
                            percentageStorageMock,
                            serviceCallStorageMock,
                            fixedClock
                    );

                    List<Integer> numbers = List.of(9, 6);
                    String referenceId = "123";

                    calculationProcessService.executeCalculationProcess(numbers, referenceId);
                }
        );
    }
}
