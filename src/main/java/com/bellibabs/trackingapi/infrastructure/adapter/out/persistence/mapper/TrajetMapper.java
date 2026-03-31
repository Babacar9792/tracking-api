package com.bellibabs.trackingapi.infrastructure.adapter.out.persistence.mapper;

import com.bellibabs.trackingapi.domain.model.Trajet;
import com.bellibabs.trackingapi.infrastructure.adapter.in.dto.TrajetDto;
import com.bellibabs.trackingapi.infrastructure.adapter.out.persistence.entity.TrajetEntity;

public class TrajetMapper {

    private TrajetMapper() {}

    public static TrajetDto toDto(Trajet trajet, String baseUrl) {
        TrajetDto dto = new TrajetDto();
        dto.setId(trajet.getId());
        dto.setClientId(trajet.getClientId());
        dto.setStatut(trajet.getStatut().name());
        dto.setShareToken(trajet.getShareToken());
        dto.setCreatedAt(trajet.getCreatedAt());
        dto.setTrackingUrl(baseUrl + "/tracking/" + trajet.getShareToken());
        return dto;
    }

    public static Trajet toDomain(TrajetEntity entity) {
        return new Trajet(
                entity.getId(),
                entity.getClientId(),
                entity.getStatut(),
                entity.getShareToken(),
                entity.getCreatedAt()
        );
    }

    public static TrajetEntity toEntityFromDomain(Trajet trajet) {
        return new TrajetEntity(
                trajet.getId(),
                trajet.getClientId(),
                trajet.getStatut(),
                trajet.getShareToken(),
                trajet.getCreatedAt()
        );
    }
}
