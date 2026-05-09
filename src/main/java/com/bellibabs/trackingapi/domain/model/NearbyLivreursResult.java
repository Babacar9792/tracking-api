package com.bellibabs.trackingapi.domain.model;

import java.util.List;

public record NearbyLivreursResult(List<LivreurPosition> livreurs, double effectiveRadiusKm) {
}
