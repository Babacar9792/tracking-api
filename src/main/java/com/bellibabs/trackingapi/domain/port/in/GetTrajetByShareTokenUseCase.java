package com.bellibabs.trackingapi.domain.port.in;

import com.bellibabs.trackingapi.domain.model.Trajet;

import java.util.UUID;

public interface GetTrajetByShareTokenUseCase {
    Trajet getTrajetByShareToken(UUID shareToken);
}
