package com.bellibabs.trackingapi.infrastructure.adapter.in.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TrajetDto {
    private UUID id;
    private String clientId;
    private String statut;
    private UUID shareToken;
    private LocalDateTime createdAt;
    private String trackingUrl;
}
