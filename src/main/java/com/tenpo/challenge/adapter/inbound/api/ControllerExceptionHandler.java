package com.tenpo.challenge.adapter.inbound.api;

import com.tenpo.challenge.adapter.inbound.api.dto.ErrorResponseDto;
import com.tenpo.challenge.adapter.inbound.api.exception.InvalidAttributeException;
import com.tenpo.challenge.adapter.inbound.api.exception.MaxRequestsNumberAchievedException;
import com.tenpo.challenge.adapter.outbound.external.postman.exception.PostmanClientException;
import com.tenpo.challenge.domain.exception.PercentageNotRetrievedException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.time.Clock;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@ControllerAdvice
public class ControllerExceptionHandler {

    private final Logger logger = LoggerFactory.getLogger(ControllerExceptionHandler.class);

    private final Clock clock;

    private final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");

    public ControllerExceptionHandler(Clock clock) {
        this.clock = clock;
    }

    @ExceptionHandler(InvalidAttributeException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public ErrorResponseDto handleBindException(InvalidAttributeException ex) {
        logger.error("Invalid attribute error",ex);
        return ErrorResponseDto.builder()
                .description(ex.getMessage())
                .timestamp(dateTimeFormatter.format(LocalDateTime.now(clock)))
                .build();
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public ErrorResponseDto handleBindException(HttpMessageNotReadableException ex) {
        logger.error("Invalid attribute error",ex);
        return ErrorResponseDto.builder()
                .description("Invalid attribute error")
                .timestamp(dateTimeFormatter.format(LocalDateTime.now(clock)))
                .build();
    }

    @ExceptionHandler(PostmanClientException.class)
    @ResponseStatus(HttpStatus.BAD_GATEWAY)
    @ResponseBody
    public ErrorResponseDto handleBindException(PostmanClientException ex) {
        logger.error("External service error",ex);
        return ErrorResponseDto.builder()
                .description(ex.getMessage())
                .timestamp(dateTimeFormatter.format(LocalDateTime.now(clock)))
                .build();
    }

    @ExceptionHandler(PercentageNotRetrievedException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ResponseBody
    public ErrorResponseDto handleBindException(PercentageNotRetrievedException ex) {
        logger.error("Percentage not retrieved",ex);
        return ErrorResponseDto.builder()
                .description(ex.getMessage())
                .timestamp(dateTimeFormatter.format(LocalDateTime.now(clock)))
                .build();
    }

    @ExceptionHandler(MaxRequestsNumberAchievedException.class)
    public ResponseEntity<ErrorResponseDto> handleBindException(MaxRequestsNumberAchievedException ex) {
        logger.error("Max requests number achieved", ex);
        return ResponseEntity.status(HttpStatus.TOO_MANY_REQUESTS)
                .header("retry-after","60")
                .body(ErrorResponseDto.builder()
                        .description("Max requests number achieved")
                        .timestamp(dateTimeFormatter.format(LocalDateTime.now(clock)))
                        .build()
                );
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ResponseBody
    public ErrorResponseDto handleBindException(Exception ex) {
        logger.error("Unknown error",ex);
        return ErrorResponseDto.builder()
                .description(ex.getMessage())
                .timestamp(dateTimeFormatter.format(LocalDateTime.now(clock)))
                .build();
    }


}
