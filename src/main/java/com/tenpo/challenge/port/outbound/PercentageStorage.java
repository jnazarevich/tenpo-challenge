package com.tenpo.challenge.port.outbound;

import com.tenpo.challenge.domain.model.PercentageData;

import java.util.Optional;

public interface PercentageStorage {

    PercentageData store(PercentageData percentageData);

    Optional<PercentageData> findLast();
}
