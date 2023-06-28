package com.tenpo.challenge.port.outbound;

import com.tenpo.challenge.adapter.outbound.postgres.ServiceCallEntity;
import com.tenpo.challenge.domain.model.ServiceCallPageable;
import com.tenpo.challenge.domain.model.ServiceCallTypeEnum;
import com.tenpo.challenge.domain.model.ServiceCall;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.List;

public interface ServiceCallStorage {
    List<ServiceCall> findByPeriodAndType(final LocalDateTime from, final LocalDateTime to, final ServiceCallTypeEnum serviceCallTypeEnum);

    ServiceCallEntity store(final ServiceCallEntity serviceCallEntity);

    Page<ServiceCallPageable> findAll(final Pageable pageable);


}
