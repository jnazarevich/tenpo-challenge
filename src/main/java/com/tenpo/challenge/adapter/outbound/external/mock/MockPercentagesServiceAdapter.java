package com.tenpo.challenge.adapter.outbound.external.mock;

import com.tenpo.challenge.domain.model.PercentageData;
import com.tenpo.challenge.port.outbound.PercentagesManager;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.Clock;
import java.time.LocalDateTime;

@Component
public class MockPercentagesServiceAdapter implements PercentagesManager {

    private final Clock clock;

    public MockPercentagesServiceAdapter(final Clock clock){
        this.clock = clock;
    }

    @Override
    public PercentageData retrieveData() {
        return new PercentageData(BigDecimal.TEN, LocalDateTime.now(clock));
    }
}
