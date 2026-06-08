package com.bellibabs.trackingapi.domain.port.in;

import com.bellibabs.trackingapi.domain.model.Trajet;

public interface GetActiveTrajetByClientIdUseCase {
    Trajet getActiveTrajet(String clientId);
}
