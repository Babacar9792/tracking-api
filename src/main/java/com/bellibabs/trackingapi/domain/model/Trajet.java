package com.bellibabs.trackingapi.domain.model;

import java.time.LocalDateTime;
import java.util.UUID;

public class Trajet {

    private UUID id;
    private String clientId;
    private TrajetStatut statut;
    private UUID shareToken;
    private LocalDateTime createdAt;
    private Double departureLatitude;
    private Double departureLongitude;
    private Double arrivalLatitude;
    private Double arrivalLongitude;

    public Trajet() {}

    public Trajet(UUID id, String clientId, TrajetStatut statut, UUID shareToken, LocalDateTime createdAt,
                  Double departureLatitude, Double departureLongitude,
                  Double arrivalLatitude, Double arrivalLongitude) {
        this.id = id;
        this.clientId = clientId;
        this.statut = statut;
        this.shareToken = shareToken;
        this.createdAt = createdAt;
        this.departureLatitude = departureLatitude;
        this.departureLongitude = departureLongitude;
        this.arrivalLatitude = arrivalLatitude;
        this.arrivalLongitude = arrivalLongitude;
    }

    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }

    public String getClientId() { return clientId; }
    public void setClientId(String clientId) { this.clientId = clientId; }

    public TrajetStatut getStatut() { return statut; }
    public void setStatut(TrajetStatut statut) { this.statut = statut; }

    public UUID getShareToken() { return shareToken; }
    public void setShareToken(UUID shareToken) { this.shareToken = shareToken; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public Double getDepartureLatitude() { return departureLatitude; }
    public void setDepartureLatitude(Double departureLatitude) { this.departureLatitude = departureLatitude; }

    public Double getDepartureLongitude() { return departureLongitude; }
    public void setDepartureLongitude(Double departureLongitude) { this.departureLongitude = departureLongitude; }

    public Double getArrivalLatitude() { return arrivalLatitude; }
    public void setArrivalLatitude(Double arrivalLatitude) { this.arrivalLatitude = arrivalLatitude; }

    public Double getArrivalLongitude() { return arrivalLongitude; }
    public void setArrivalLongitude(Double arrivalLongitude) { this.arrivalLongitude = arrivalLongitude; }
}
