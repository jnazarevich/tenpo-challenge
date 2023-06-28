package com.tenpo.challenge.adapter.inbound.api;

import com.tenpo.challenge.domain.model.ServiceCallPageable;
import com.tenpo.challenge.port.outbound.ServiceCallStorage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ServiceCallController {

    private final ServiceCallStorage serviceCallStorage;

    @Autowired
    public ServiceCallController(ServiceCallStorage serviceCallStorage) {
        this.serviceCallStorage = serviceCallStorage;
    }

    @GetMapping("/service-calls")
    public Page<ServiceCallPageable> findAll(Pageable pageable){
        return this.serviceCallStorage.findAll(pageable);
    }
}
