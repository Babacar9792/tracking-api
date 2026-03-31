package com.bellibabs.trackingapi.infrastructure.adapter.in.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PositionEventDto {
    private UUID id;
    private UUID trajetId;
    private Double latitude;
    private Double longitude;
    private Instant timestamp;
}
