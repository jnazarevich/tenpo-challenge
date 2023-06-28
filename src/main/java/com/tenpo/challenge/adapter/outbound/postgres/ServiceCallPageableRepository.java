package com.tenpo.challenge.adapter.outbound.postgres;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ServiceCallPageableRepository extends PagingAndSortingRepository<ServiceCallEntity,Long> {
}
