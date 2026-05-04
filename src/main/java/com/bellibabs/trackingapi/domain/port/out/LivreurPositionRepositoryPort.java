package com.bellibabs.trackingapi.domain.port.out;

import com.bellibabs.trackingapi.domain.model.LivreurPosition;

import java.util.List;

public interface LivreurPositionRepositoryPort {
    void save(LivreurPosition position);
    List<LivreurPosition> findNearby(Double latitude, Double longitude, Double radiusKm);
}