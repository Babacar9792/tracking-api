package com.bellibabs.trackingapi.domain.port.in;

import com.bellibabs.trackingapi.domain.model.Trajet;

public interface CreateTrajetUseCase {
    Trajet createTrajet(String clientId);
}
