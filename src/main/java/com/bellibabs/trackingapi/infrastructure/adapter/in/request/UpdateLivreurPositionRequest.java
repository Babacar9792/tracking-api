package com.bellibabs.trackingapi.infrastructure.adapter.in.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class UpdateLivreurPositionRequest {

    @NotBlank(message = "livreurId est obligatoire")
    private String livreurId;

    @NotNull(message = "latitude est obligatoire")
    private Double latitude;

    @NotNull(message = "longitude est obligatoire")
    private Double longitude;
}