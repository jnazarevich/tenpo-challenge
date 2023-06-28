package com.tenpo.challenge.adapter.outbound.external.postman;

import com.tenpo.challenge.adapter.outbound.external.postman.client.PostmanClient;
import com.tenpo.challenge.adapter.outbound.external.postman.dto.PercentageDataResponse;
import com.tenpo.challenge.adapter.outbound.external.postman.exception.PostmanClientException;
import com.tenpo.challenge.adapter.outbound.external.postman.mapper.PostmanMapper;
import com.tenpo.challenge.domain.exception.PercentageNotRetrievedException;
import com.tenpo.challenge.domain.model.PercentageData;
import com.tenpo.challenge.port.outbound.PercentagesManager;
import org.springframework.stereotype.Component;

@Component
public class PostmanServerAdapter implements PercentagesManager {

    private final PostmanClient postmanClient;

    private final PostmanMapper postmanMapper;

    public PostmanServerAdapter(final PostmanClient postmanClient, final PostmanMapper postmanMapper){
        this.postmanClient = postmanClient;
        this.postmanMapper = postmanMapper;
    }

    @Override
    public PercentageData retrieveData() {
        try {
            PercentageDataResponse percentageDataResponse = postmanClient.retrievePercentageData();
            return this.postmanMapper.dtoToModel(percentageDataResponse);
        } catch (PostmanClientException e) {
            throw new PercentageNotRetrievedException(e.getMessage(), e);
        }
    }
}
