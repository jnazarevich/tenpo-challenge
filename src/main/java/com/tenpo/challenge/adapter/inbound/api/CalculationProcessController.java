package com.tenpo.challenge.adapter.inbound.api;

import com.tenpo.challenge.adapter.inbound.api.dto.CalculationProcessRequestDto;
import com.tenpo.challenge.adapter.inbound.api.dto.CalculationProcessResponseDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CalculationProcessController {

    private final CalculationProcessApiService calculationProcessApiService;

    @Autowired
    public CalculationProcessController(final CalculationProcessApiService calculationProcessApiService) {
        this.calculationProcessApiService = calculationProcessApiService;
    }

    @PostMapping(
            path = "/processes",
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<CalculationProcessResponseDto> executeCalculationProcess(
            final @RequestBody CalculationProcessRequestDto calculationProcessRequestDto
    ) {
        return ResponseEntity
                .ok()
                // En caso que se elija la forma ASYNC se devuelve 202
                //accepted()
                .body(this.calculationProcessApiService.executeCalculationProcess(calculationProcessRequestDto));
    }
}
