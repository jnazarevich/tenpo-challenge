package com.tenpo.challenge.configuration;

import com.tenpo.challenge.domain.process.CalculationProcessService;
import com.tenpo.challenge.port.outbound.*;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Clock;

@Configuration
public class UseCaseConfiguration {

    @Bean
    public CalculationProcessService calculationProcessUseCase(
            @Qualifier("postmanServerAdapter")
            final PercentagesManager percentagesManager,
            final ProcessStorage processStorage,
            final UUIDStorage uuidStorage,
            final CallbackManager callbackManager,
            final PercentageStorage percentageStorage,
            final ServiceCallStorage serviceCallStorage,
            final Clock clock
            ){
        return new CalculationProcessService(
                percentagesManager,
                processStorage,
                uuidStorage,
                callbackManager,
                percentageStorage,
                serviceCallStorage,
                clock
        );
    }
}
