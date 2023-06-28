package com.tenpo.challenge.adapter.outbound.postgres;

import com.tenpo.challenge.domain.model.ServiceCall;
import com.tenpo.challenge.domain.model.ServiceCallPageable;
import com.tenpo.challenge.domain.model.ServiceCallTypeEnum;
import com.tenpo.challenge.port.outbound.ServiceCallStorage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class ServiceCallR2DBCAdapter implements ServiceCallStorage {

    private final ServiceCallRepository serviceCallRepository;

    private final ServiceCallPageableRepository serviceCallPageableRepository;

    @Autowired
    public ServiceCallR2DBCAdapter(final ServiceCallRepository serviceCallRepository, final ServiceCallPageableRepository serviceCallPageableRepository) {
        this.serviceCallRepository = serviceCallRepository;
        this.serviceCallPageableRepository = serviceCallPageableRepository;
    }

    @Override
    public ServiceCallEntity store(final ServiceCallEntity serviceCallEntity) {
        return this.serviceCallRepository.save(serviceCallEntity);
    }

    @Override
    public Page<ServiceCallPageable> findAll(Pageable pageable) {
        return this.serviceCallPageableRepository.findAll(pageable)
                .map(serviceCallEntity ->
                        new ServiceCallPageable(
                                serviceCallEntity.getHttpMethod(),
                                serviceCallEntity.getUrl(),
                                serviceCallEntity.getHeaders(),
                                serviceCallEntity.getRequest(),
                                serviceCallEntity.getResponse(),
                                serviceCallEntity.getHttpStatus(),
                                serviceCallEntity.getType(),
                                serviceCallEntity.getCreatedAt()
                        )
                );
    }

    @Override
    public List<ServiceCall> findByPeriodAndType(final LocalDateTime from, final LocalDateTime to, final ServiceCallTypeEnum serviceCallTypeEnum) {
        return this.serviceCallRepository.findByCreatedAtGreaterThanEqualAndCreatedAtLessThanEqualAndType(from,to, serviceCallTypeEnum.name())
                .stream()
                .map(serviceCallEntity ->
                        new ServiceCall(serviceCallEntity.getCreatedAt(), ServiceCallTypeEnum.valueOf(serviceCallEntity.getType()))
                )
                .collect(Collectors.toList());
    }


}
