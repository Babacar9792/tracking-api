package com.bellibabs.trackingapi.infrastructure.adapter.out.persistence.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "position_events", indexes = {
        @Index(name = "idx_position_trajet_id", columnList = "trajetId")
})
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PositionEventEntity {

    @Id
    private UUID id;

    @Column(nullable = false)
    private UUID trajetId;

    @Column(nullable = false)
    private Double latitude;

    @Column(nullable = false)
    private Double longitude;

    @Column(nullable = false)
    private Instant timestamp;
}
