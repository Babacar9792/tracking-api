package com.bellibabs.trackingapi.infrastructure.adapter.in.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class CreateTrajetRequest {

    @NotBlank(message = "clientId est obligatoire")
    private String clientId;

    @NotNull(message = "departureLatitude est obligatoire")
    private Double departureLatitude;

    @NotNull(message = "departureLongitude est obligatoire")
    private Double departureLongitude;

    private Double arrivalLatitude;

    private Double arrivalLongitude;
}
