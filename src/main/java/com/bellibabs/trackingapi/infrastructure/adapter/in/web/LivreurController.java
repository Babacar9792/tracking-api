package com.bellibabs.trackingapi.infrastructure.adapter.in.web;

import com.bellibabs.trackingapi.domain.model.NearbyLivreursResult;
import com.bellibabs.trackingapi.domain.port.in.GetNearbyLivreursUseCase;
import com.bellibabs.trackingapi.infrastructure.adapter.in.dto.ApiResponse;
import com.bellibabs.trackingapi.infrastructure.adapter.in.dto.NearbyLivreurDto;
import com.bellibabs.trackingapi.infrastructure.adapter.out.persistence.mapper.LivreurPositionMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/livreurs")
@RequiredArgsConstructor
@Validated
@Tag(name = "Livreurs", description = "Suivi temps réel des livreurs et recherche de proximité")
public class LivreurController {

    private final GetNearbyLivreursUseCase getNearbyLivreursUseCase;

    @Operation(
            summary = "Livreurs à proximité",
            description = "Recherche les livreurs disponibles en partant d'un rayon de 5 km, doublé automatiquement jusqu'au maximum configuré si aucun résultat n'est trouvé. Résultats triés par distance ASC."
    )
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Liste des livreurs proches",
                    content = @Content(schema = @Schema(implementation = NearbyLivreurDto.class))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Paramètres invalides", content = @Content)
    })
    @GetMapping("/nearby")
    public ResponseEntity<ApiResponse<List<NearbyLivreurDto>>> getNearbyLivreurs(
            @Parameter(description = "Latitude du point de départ")
            @RequestParam @NotNull @DecimalMin("-90.0") @DecimalMax("90.0") Double latitude,

            @Parameter(description = "Longitude du point de départ")
            @RequestParam @NotNull @DecimalMin("-180.0") @DecimalMax("180.0") Double longitude
    ) {
        log.info("event.action=GET_NEARBY_LIVREURS_REQUEST, event.outcome=RECEIVED, lat={}, lng={}", latitude, longitude);

        NearbyLivreursResult result = getNearbyLivreursUseCase.getNearbyLivreurs(latitude, longitude);
        List<NearbyLivreurDto> dtos = result.livreurs().stream()
                .map(LivreurPositionMapper::toDto)
                .toList();

        log.info("event.action=GET_NEARBY_LIVREURS_REQUEST, event.outcome=SUCCESS, count={}, effectiveRadiusKm={}",
                dtos.size(), result.effectiveRadiusKm());

        String message = dtos.isEmpty()
                ? "Aucun livreur disponible dans un rayon de " + result.effectiveRadiusKm() + " km (rayon maximum atteint)"
                : dtos.size() + " livreur(s) trouvé(s) dans un rayon de " + result.effectiveRadiusKm() + " km";

        return ResponseEntity.ok(new ApiResponse<>(message, dtos));
    }
}