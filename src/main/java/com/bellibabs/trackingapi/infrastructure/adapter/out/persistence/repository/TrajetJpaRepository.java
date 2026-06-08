package com.bellibabs.trackingapi.infrastructure.adapter.out.persistence.repository;

import com.bellibabs.trackingapi.domain.model.TrajetStatut;
import com.bellibabs.trackingapi.infrastructure.adapter.out.persistence.entity.TrajetEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface TrajetJpaRepository extends JpaRepository<TrajetEntity, UUID> {
    Optional<TrajetEntity> findByShareToken(UUID shareToken);
    Optional<TrajetEntity> findFirstByClientIdAndStatutInOrderByCreatedAtDesc(String clientId, List<TrajetStatut> statuts);
}
