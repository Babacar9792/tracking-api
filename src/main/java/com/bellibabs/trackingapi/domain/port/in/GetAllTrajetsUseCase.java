package com.bellibabs.trackingapi.domain.port.in;

import com.bellibabs.trackingapi.domain.model.Trajet;

import java.util.List;

public interface GetAllTrajetsUseCase {
    List<Trajet> getAllTrajets();
}
