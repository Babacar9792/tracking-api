package com.bellibabs.trackingapi.infrastructure.adapter.out.persistence.mapper;

import com.bellibabs.trackingapi.domain.model.LivreurPosition;
import com.bellibabs.trackingapi.infrastructure.adapter.in.dto.NearbyLivreurDto;

public class LivreurPositionMapper {

    private LivreurPositionMapper() {}

    public static NearbyLivreurDto toDto(LivreurPosition domain) {
        return new NearbyLivreurDto(
                domain.getLivreurId(),
                domain.getLatitude(),
                domain.getLongitude(),
                domain.getDistanceKm(),
                domain.getUpdatedAt()
        );
    }
}