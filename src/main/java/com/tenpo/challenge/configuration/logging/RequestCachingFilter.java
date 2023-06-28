package com.tenpo.challenge.configuration.logging;

import com.tenpo.challenge.adapter.outbound.postgres.ServiceCallEntity;
import com.tenpo.challenge.adapter.outbound.postgres.ServiceCallR2DBCAdapter;
import com.tenpo.challenge.domain.model.ServiceCallTypeEnum;
import com.tenpo.challenge.port.outbound.ServiceCallStorage;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.time.Clock;
import java.time.LocalDateTime;
import java.util.Enumeration;
import java.util.concurrent.CompletableFuture;

@Order(value = Ordered.HIGHEST_PRECEDENCE)
@Component
@WebFilter(filterName = "RequestCachingFilter", urlPatterns = "/*")
public class RequestCachingFilter extends OncePerRequestFilter {

    private final static Logger LOGGER = LoggerFactory.getLogger(RequestCachingFilter.class);

    private final ServiceCallStorage serviceCallStorage;

    private final Clock clock;

    @Autowired
    public RequestCachingFilter(final Clock clock, final ServiceCallStorage serviceCallStorage) {
        this.clock = clock;
        this.serviceCallStorage = serviceCallStorage;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        CachedHttpServletRequest cachedHttpServletRequest = new CachedHttpServletRequest(request);
        CachedHttpServletResponse cachedHttpServletResponse = new CachedHttpServletResponse(response);

        String requestBody = new String(cachedHttpServletRequest.getContentAsByteArray());
        LOGGER.info("REQUEST DATA: " + requestBody);

        StringBuilder sb = new StringBuilder();

        Enumeration<String> headerNames = cachedHttpServletRequest.getHeaderNames();

        if (headerNames != null) {
            while (headerNames.hasMoreElements()) {
                String nextElement = headerNames.nextElement();
                sb.append(nextElement)
                        .append("=")
                        .append(cachedHttpServletRequest.getHeader(nextElement))
                        .append(";");
            }
        }

        StringBuilder requestURL = new StringBuilder(request.getRequestURL().toString());
        String queryString = request.getQueryString();

        if (queryString != null) {
            requestURL.append('?').append(queryString);
        }
        String httpMethod = cachedHttpServletRequest.getMethod();

        CompletableFuture<ServiceCallEntity> completableFuture = CompletableFuture.supplyAsync(() -> serviceCallStorage.store(
                        ServiceCallEntity.builder()
                                .headers(sb.toString())
                                .url(requestURL.toString())
                                .httpMethod(httpMethod)
                                .request(requestBody)
                                .type(ServiceCallTypeEnum.INCOMING.name())
                                .createdAt(LocalDateTime.now(clock))
                                .build()
                )
        );

        filterChain.doFilter(cachedHttpServletRequest,cachedHttpServletResponse);

        String responseBody = new String(cachedHttpServletResponse.getContentAsByteArray());
        String httpStatus = String.valueOf(cachedHttpServletResponse.getStatus());

        completableFuture.thenApply(storedServiceCall -> serviceCallStorage.store(
                        ServiceCallEntity.builder()
                                .id(storedServiceCall.getId())
                                .headers(storedServiceCall.getHeaders()!=null ? storedServiceCall.getHeaders() : null)
                                .url(storedServiceCall.getUrl()!=null ? storedServiceCall.getUrl() : null)
                                .httpMethod(storedServiceCall.getHttpMethod()!=null ? storedServiceCall.getHttpMethod() : null)
                                .request(storedServiceCall.getRequest()!=null ? storedServiceCall.getRequest() : null)
                                .response(responseBody)
                                .httpStatus(httpStatus)
                                .type(storedServiceCall.getType()!=null ? storedServiceCall.getType() : null)
                                .createdAt(storedServiceCall.getCreatedAt()!=null ? storedServiceCall.getCreatedAt() : null)
                                .build()
                )
        );

        LOGGER.info("RESPONSE DATA: " + responseBody);

    }
}
