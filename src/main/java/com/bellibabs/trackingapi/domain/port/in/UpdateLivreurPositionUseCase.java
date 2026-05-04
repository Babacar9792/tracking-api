package com.bellibabs.trackingapi.domain.port.in;

import com.bellibabs.trackingapi.domain.model.LivreurPosition;

public interface UpdateLivreurPositionUseCase {
    LivreurPosition updatePosition(String livreurId, Double latitude, Double longitude);
}