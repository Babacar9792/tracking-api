package com.bellibabs.trackingapi.domain.port.out;

import com.bellibabs.trackingapi.domain.model.PositionEvent;

import java.util.List;
import java.util.UUID;

public interface PositionRepositoryPort {
    PositionEvent save(PositionEvent event);
    List<PositionEvent> findByTrajetIdOrderByTimestampAsc(UUID trajetId);
}
