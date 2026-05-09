package com.bellibabs.trackingapi.domain.port.in;

import com.bellibabs.trackingapi.domain.model.NearbyLivreursResult;

public interface GetNearbyLivreursUseCase {
    NearbyLivreursResult getNearbyLivreurs(Double latitude, Double longitude);
}
