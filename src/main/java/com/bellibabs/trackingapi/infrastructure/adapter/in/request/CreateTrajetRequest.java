package com.bellibabs.trackingapi.infrastructure.adapter.in.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class CreateTrajetRequest {

    @NotBlank(message = "clientId est obligatoire")
    private String clientId;
}
