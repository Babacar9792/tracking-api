package com.bellibabs.trackingapi.infrastructure.adapter.out.persistence.mapper;

import com.bellibabs.trackingapi.domain.model.PositionEvent;
import com.bellibabs.trackingapi.infrastructure.adapter.in.dto.PositionEventDto;
import com.bellibabs.trackingapi.infrastructure.adapter.out.persistence.entity.PositionEventEntity;

public class PositionEventMapper {

    private PositionEventMapper() {}

    public static PositionEventDto toDto(PositionEvent event) {
        return new PositionEventDto(
                event.getId(),
                event.getTrajetId(),
                event.getLatitude(),
                event.getLongitude(),
                event.getTimestamp()
        );
    }

    public static PositionEvent toDomain(PositionEventEntity entity) {
        return new PositionEvent(
                entity.getId(),
                entity.getTrajetId(),
                entity.getLatitude(),
                entity.getLongitude(),
                entity.getTimestamp()
        );
    }

    public static PositionEventEntity toEntityFromDomain(PositionEvent event) {
        return new PositionEventEntity(
                event.getId(),
                event.getTrajetId(),
                event.getLatitude(),
                event.getLongitude(),
                event.getTimestamp()
        );
    }
}
