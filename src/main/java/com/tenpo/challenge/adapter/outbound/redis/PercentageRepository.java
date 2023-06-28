package com.tenpo.challenge.adapter.outbound.redis;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PercentageRepository extends CrudRepository<PercentageEntity,Long> {

    Optional<PercentageEntity> findFirstByOrderByCreatedAtDesc();

}
