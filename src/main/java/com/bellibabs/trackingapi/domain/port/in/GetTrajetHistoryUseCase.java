package com.bellibabs.trackingapi.domain.port.in;

import com.bellibabs.trackingapi.domain.model.PositionEvent;

import java.util.List;
import java.util.UUID;

public interface GetTrajetHistoryUseCase {
    List<PositionEvent> getTrajetHistory(UUID trajetId);
}
