package com.tenpo.challenge.adapter.outbound.external.postman.client;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tenpo.challenge.adapter.outbound.external.postman.dto.PercentageDataResponse;
import com.tenpo.challenge.adapter.outbound.external.postman.exception.PostmanClientException;
import com.tenpo.challenge.adapter.outbound.postgres.ServiceCallEntity;
import com.tenpo.challenge.adapter.outbound.postgres.ServiceCallR2DBCAdapter;
import com.tenpo.challenge.domain.model.ServiceCallTypeEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.client.UnknownHttpStatusCodeException;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;
import java.net.URI;
import java.time.Clock;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;

@Component
public class PostmanClient {

    private final RestTemplate restTemplate;

    private final ObjectMapper objectMapper;

    private final ServiceCallR2DBCAdapter serviceCallR2DBCAdapter;

    private final Clock clock;

    @Value("${postman-client.url}")
    private String url;

    @Value("${postman-client.path.retrieve-percentage-data}")
    private String retrievePercentageDataPath;

    @Value("${postman-client.query.retrieve-percentage-data}")
    private String retrievePercentageDataQuery;


    @Autowired
    public PostmanClient(
            final RestTemplate restTemplate,
            final ObjectMapper objectMapper,
            final ServiceCallR2DBCAdapter serviceCallR2DBCAdapter,
            final Clock clock
    ){
        this.restTemplate = restTemplate;
        this.objectMapper = objectMapper;
        this.serviceCallR2DBCAdapter = serviceCallR2DBCAdapter;
        this.clock = clock;
    }


    @Retryable(maxAttempts = 3, retryFor = PostmanClientException.class)
    public PercentageDataResponse retrievePercentageData(){
        URI uri = UriComponentsBuilder
                .fromUriString(this.url)
                .path(retrievePercentageDataPath)
                .queryParam("status",retrievePercentageDataQuery)
                .build()
                .toUri();

        HttpMethod httpMethod = HttpMethod.GET;

        CompletableFuture<ServiceCallEntity> completableFuture = CompletableFuture.supplyAsync(() ->
                        this.serviceCallR2DBCAdapter.store(
                                ServiceCallEntity.builder()
                                        .url(uri.toString())
                                        .httpMethod(httpMethod.name())
                                        .createdAt(LocalDateTime.now(clock))
                                        .type(ServiceCallTypeEnum.OUTGOING.name())
                                        .build()
                        )
        );

        ResponseEntity<String> responseEntity;

        try {
            responseEntity = this.restTemplate
                    .exchange(
                            uri,
                            httpMethod,
                            null,
                            String.class
                    );
        } catch (HttpServerErrorException | HttpClientErrorException | UnknownHttpStatusCodeException ex) {
            responseEntity = ResponseEntity
                    .status(ex.getStatusCode())
                    .body(ex.getResponseBodyAsString());
        }

        return this.handleResponse(responseEntity,PercentageDataResponse.class,completableFuture);
    }

    private <T>T handleResponse(ResponseEntity<String> responseEntity, Class<T> responseBody, CompletableFuture<ServiceCallEntity> completableFuture){
        String responseBodyString = responseEntity.getBody();

        completableFuture.thenApply(serviceCallEntityStored ->
                        this.serviceCallR2DBCAdapter.store(
                                ServiceCallEntity
                                        .builder()
                                        .id(serviceCallEntityStored.getId())
                                        .request(serviceCallEntityStored.getRequest()!=null ? serviceCallEntityStored.getRequest() : null)
                                        .headers(serviceCallEntityStored.getHeaders()!=null ? serviceCallEntityStored.getHeaders() : null)
                                        .httpMethod(serviceCallEntityStored.getHttpMethod()!=null ? serviceCallEntityStored.getHttpMethod() : null)
                                        .url(serviceCallEntityStored.getUrl())
                                        .response(responseBodyString)
                                        .httpStatus(String.valueOf(responseEntity.getStatusCode().value()))
                                        .type(serviceCallEntityStored.getType()!=null ? serviceCallEntityStored.getType() : null)
                                        .createdAt(serviceCallEntityStored.getCreatedAt()!=null ? serviceCallEntityStored.getCreatedAt() : null)
                                        .build()
                        )
                );

        if (responseEntity.getStatusCode().is2xxSuccessful() && !Objects.requireNonNull(responseEntity.getBody()).isEmpty())
            try {
                return objectMapper.readValue(responseBodyString,responseBody);
            } catch (IOException e) {
                throw new PostmanClientException("Error mapping message from provider", e);
            }
        else {
            throw new PostmanClientException(
                    "Status code from provider:" + responseEntity.getStatusCode().value() +
                            "Message from provider: " + responseBodyString
            );
        }
    }
}
