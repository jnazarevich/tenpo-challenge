package com.tenpo.challenge.adapter.outbound.postgres;

import com.tenpo.challenge.domain.model.Process;
import com.tenpo.challenge.port.outbound.ProcessStorage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ProcessR2DBCAdapter implements ProcessStorage {

    private final ProcessRepository processRepository;

    private final ProcessMapper processMapper;

    @Autowired
    ProcessR2DBCAdapter(final ProcessRepository processRepository, final ProcessMapper processMapper){
        this.processRepository = processRepository;
        this.processMapper = processMapper;
    }

    @Override
    public Process store(String referenceId, String status, String callbackUrl) {
        return processMapper.entityToModel(
                processRepository.save(
                        ProcessEntity
                                .builder()
                                .referenceId(referenceId)
                                .status(status)
                                .callbackUrl(callbackUrl)
                                .build()
                )
        );
    }
}
