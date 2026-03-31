package com.bellibabs.trackingapi.domain.model;

import java.time.Instant;
import java.util.UUID;

public class PositionEvent {

    private UUID id;
    private UUID trajetId;
    private Double latitude;
    private Double longitude;
    private Instant timestamp;

    public PositionEvent() {}

    public PositionEvent(UUID id, UUID trajetId, Double latitude, Double longitude, Instant timestamp) {
        this.id = id;
        this.trajetId = trajetId;
        this.latitude = latitude;
        this.longitude = longitude;
        this.timestamp = timestamp;
    }

    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }

    public UUID getTrajetId() { return trajetId; }
    public void setTrajetId(UUID trajetId) { this.trajetId = trajetId; }

    public Double getLatitude() { return latitude; }
    public void setLatitude(Double latitude) { this.latitude = latitude; }

    public Double getLongitude() { return longitude; }
    public void setLongitude(Double longitude) { this.longitude = longitude; }

    public Instant getTimestamp() { return timestamp; }
    public void setTimestamp(Instant timestamp) { this.timestamp = timestamp; }
}
