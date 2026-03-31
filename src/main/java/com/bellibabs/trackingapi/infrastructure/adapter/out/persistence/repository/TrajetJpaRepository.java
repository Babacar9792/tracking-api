package com.bellibabs.trackingapi.infrastructure.adapter.out.persistence.repository;

import com.bellibabs.trackingapi.infrastructure.adapter.out.persistence.entity.TrajetEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface TrajetJpaRepository extends JpaRepository<TrajetEntity, UUID> {
    Optional<TrajetEntity> findByShareToken(UUID shareToken);
}
