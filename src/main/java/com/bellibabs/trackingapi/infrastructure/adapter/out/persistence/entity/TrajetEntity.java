package com.bellibabs.trackingapi.infrastructure.adapter.out.persistence.entity;

import com.bellibabs.trackingapi.domain.model.TrajetStatut;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "trajets")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TrajetEntity {

    @Id
    private UUID id;

    @Column(nullable = false)
    private String clientId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TrajetStatut statut;

    @Column(unique = true, nullable = false)
    private UUID shareToken;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    private Double departureLatitude;

    private Double departureLongitude;

    private Double arrivalLatitude;

    private Double arrivalLongitude;
}
