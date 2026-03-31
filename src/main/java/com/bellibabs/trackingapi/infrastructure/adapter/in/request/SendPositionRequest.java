package com.bellibabs.trackingapi.infrastructure.adapter.in.request;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
public class SendPositionRequest {

    @NotNull(message = "trajetId est obligatoire")
    private UUID trajetId;

    @NotNull(message = "latitude est obligatoire")
    private Double latitude;

    @NotNull(message = "longitude est obligatoire")
    private Double longitude;
}
