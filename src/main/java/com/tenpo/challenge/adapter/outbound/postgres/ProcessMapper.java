package com.tenpo.challenge.adapter.outbound.postgres;

import com.tenpo.challenge.domain.model.Process;
import org.springframework.stereotype.Component;

@Component
public class ProcessMapper {

    Process entityToModel(ProcessEntity processEntity){
        return new Process(
                processEntity.getReferenceId(),
                processEntity.getStatus()
        );
    }
}
