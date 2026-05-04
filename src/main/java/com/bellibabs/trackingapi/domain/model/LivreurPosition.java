package com.bellibabs.trackingapi.domain.model;

import java.time.Instant;

public class LivreurPosition {

    private String livreurId;
    private Double latitude;
    private Double longitude;
    private Double distanceKm;
    private Instant updatedAt;

    public LivreurPosition() {}

    public LivreurPosition(String livreurId, Double latitude, Double longitude, Instant updatedAt) {
        this.livreurId = livreurId;
        this.latitude = latitude;
        this.longitude = longitude;
        this.updatedAt = updatedAt;
    }

    public String getLivreurId() { return livreurId; }
    public void setLivreurId(String livreurId) { this.livreurId = livreurId; }

    public Double getLatitude() { return latitude; }
    public void setLatitude(Double latitude) { this.latitude = latitude; }

    public Double getLongitude() { return longitude; }
    public void setLongitude(Double longitude) { this.longitude = longitude; }

    public Double getDistanceKm() { return distanceKm; }
    public void setDistanceKm(Double distanceKm) { this.distanceKm = distanceKm; }

    public Instant getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(Instant updatedAt) { this.updatedAt = updatedAt; }
}