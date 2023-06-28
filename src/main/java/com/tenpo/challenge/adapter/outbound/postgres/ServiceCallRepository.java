package com.tenpo.challenge.adapter.outbound.postgres;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface ServiceCallRepository extends JpaRepository<ServiceCallEntity,Long> {

    List<ServiceCallEntity> findByCreatedAtGreaterThanEqualAndCreatedAtLessThanEqualAndType(LocalDateTime from, LocalDateTime to, String type);
}
