package com.bellibabs.trackingapi.domain.port.in;

import com.bellibabs.trackingapi.domain.model.PositionEvent;

import java.util.UUID;

public interface SendPositionUseCase {
    PositionEvent sendPosition(UUID trajetId, Double latitude, Double longitude);
}
