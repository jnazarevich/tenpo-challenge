package com.tenpo.challenge.adapter.outbound.redis;

import com.tenpo.challenge.domain.model.PercentageData;
import com.tenpo.challenge.port.outbound.PercentageStorage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Clock;
import java.time.LocalDateTime;
import java.util.Optional;

@Component
public class PercentageRedisAdapter implements PercentageStorage {

    private final PercentageRepository percentageRepository;

    private final Clock clock;

    @Autowired
    public PercentageRedisAdapter(final PercentageRepository percentageRepository, final Clock clock){
        this.percentageRepository = percentageRepository;
        this.clock = clock;
    }

    @Override
    public PercentageData store(PercentageData percentageData) {
        this.percentageRepository.save(
                PercentageEntity.builder()
                        .percentageValue(percentageData.percentageValue().toString())
                        .createdAt(LocalDateTime.now(clock).toString())
                        .build()
        );
        return percentageData;
    }

    @Override
    public Optional<PercentageData> findLast() {
        return this.percentageRepository
                .findFirstByOrderByCreatedAtDesc()
                .map(p -> new PercentageData(new BigDecimal(p.getPercentageValue()).setScale(2, RoundingMode.HALF_EVEN), LocalDateTime.parse(p.getCreatedAt())));
    }
}
