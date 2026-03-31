package com.bellibabs.trackingapi.domain.model;

import java.time.LocalDateTime;
import java.util.UUID;

public class Trajet {

    private UUID id;
    private String clientId;
    private TrajetStatut statut;
    private UUID shareToken;
    private LocalDateTime createdAt;

    public Trajet() {}

    public Trajet(UUID id, String clientId, TrajetStatut statut, UUID shareToken, LocalDateTime createdAt) {
        this.id = id;
        this.clientId = clientId;
        this.statut = statut;
        this.shareToken = shareToken;
        this.createdAt = createdAt;
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
}
