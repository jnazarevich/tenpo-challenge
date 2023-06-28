package com.tenpo.challenge.adapter.outbound.external.postman.mapper;

import com.tenpo.challenge.adapter.outbound.external.postman.dto.PercentageDataResponse;
import com.tenpo.challenge.domain.model.PercentageData;
import org.springframework.stereotype.Component;

import java.time.Clock;
import java.time.LocalDateTime;

@Component
public class PostmanMapper {

    private final Clock clock;

    public PostmanMapper(final Clock clock){
        this.clock = clock;
    }

    public PercentageData dtoToModel(PercentageDataResponse percentageDataResponse){
        return new PercentageData(
                percentageDataResponse.getPercentageValue(),
                LocalDateTime.now(clock)
        );
    }
}
