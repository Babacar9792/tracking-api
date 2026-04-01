package com.bellibabs.trackingapi.domain.port.out;

import com.bellibabs.trackingapi.domain.model.Trajet;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface TrajetRepositoryPort {
    Trajet save(Trajet trajet);
    Optional<Trajet> findById(UUID id);
    Optional<Trajet> findByShareToken(UUID shareToken);
    List<Trajet> findAll();
}
