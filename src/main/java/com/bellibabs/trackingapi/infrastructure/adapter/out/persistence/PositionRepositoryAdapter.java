package com.bellibabs.trackingapi.infrastructure.adapter.out.persistence;

import com.bellibabs.trackingapi.domain.model.PositionEvent;
import com.bellibabs.trackingapi.domain.port.out.PositionRepositoryPort;
import com.bellibabs.trackingapi.infrastructure.adapter.out.persistence.mapper.PositionEventMapper;
import com.bellibabs.trackingapi.infrastructure.adapter.out.persistence.repository.PositionJpaRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.UUID;

@Slf4j
@Component
@RequiredArgsConstructor
public class PositionRepositoryAdapter implements PositionRepositoryPort {

    private final PositionJpaRepository positionJpaRepository;

    @Override
    public PositionEvent save(PositionEvent event) {
        log.debug("event.action=DB_SAVE_POSITION, event.outcome=STARTED, trajetId={}", event.getTrajetId());
        PositionEvent saved = PositionEventMapper.toDomain(
                positionJpaRepository.save(PositionEventMapper.toEntityFromDomain(event))
        );
        log.debug("event.action=DB_SAVE_POSITION, event.outcome=SUCCESS, positionId={}", saved.getId());
        return saved;
    }

    @Override
    public List<PositionEvent> findByTrajetIdOrderByTimestampAsc(UUID trajetId) {
        return positionJpaRepository.findByTrajetIdOrderByTimestampAsc(trajetId)
                .stream()
                .map(PositionEventMapper::toDomain)
                .toList();
    }
}
