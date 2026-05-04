package com.bellibabs.trackingapi.infrastructure.adapter.in.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class NearbyLivreurDto {
    private String livreurId;
    private Double latitude;
    private Double longitude;
    private Double distanceKm;
    private Instant updatedAt;
}