package com.bellibabs.trackingapi.infrastructure.adapter.out.persistence;

import com.bellibabs.trackingapi.domain.model.Trajet;
import com.bellibabs.trackingapi.domain.port.out.TrajetRepositoryPort;
import com.bellibabs.trackingapi.infrastructure.adapter.out.persistence.mapper.TrajetMapper;
import com.bellibabs.trackingapi.infrastructure.adapter.out.persistence.repository.TrajetJpaRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.UUID;

@Slf4j
@Component
@RequiredArgsConstructor
public class TrajetRepositoryAdapter implements TrajetRepositoryPort {

    private final TrajetJpaRepository trajetJpaRepository;

    @Override
    public Trajet save(Trajet trajet) {
        log.debug("event.action=DB_SAVE_TRAJET, event.outcome=STARTED, trajetId={}", trajet.getId());
        Trajet saved = TrajetMapper.toDomain(trajetJpaRepository.save(TrajetMapper.toEntityFromDomain(trajet)));
        log.debug("event.action=DB_SAVE_TRAJET, event.outcome=SUCCESS, trajetId={}", saved.getId());
        return saved;
    }

    @Override
    public Optional<Trajet> findById(UUID id) {
        return trajetJpaRepository.findById(id).map(TrajetMapper::toDomain);
    }

    @Override
    public Optional<Trajet> findByShareToken(UUID shareToken) {
        return trajetJpaRepository.findByShareToken(shareToken).map(TrajetMapper::toDomain);
    }
}
