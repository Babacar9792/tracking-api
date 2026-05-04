package com.bellibabs.trackingapi.domain.port.in;

import com.bellibabs.trackingapi.domain.model.LivreurPosition;

import java.util.List;

public interface GetNearbyLivreursUseCase {
    List<LivreurPosition> getNearbyLivreurs(Double latitude, Double longitude, Double radiusKm);
}
