package com.bellibabs.trackingapi.infrastructure.adapter.in.web;

import com.bellibabs.trackingapi.domain.model.LivreurPosition;
import com.bellibabs.trackingapi.domain.port.in.GetNearbyLivreursUseCase;
import com.bellibabs.trackingapi.infrastructure.adapter.in.dto.NearbyLivreurDto;
import com.bellibabs.trackingapi.infrastructure.adapter.out.persistence.mapper.LivreurPositionMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
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
            description = "Retourne les livreurs disponibles dans le rayon donné autour d'une adresse de départ, triés par distance ASC."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Liste des livreurs proches",
                    content = @Content(schema = @Schema(implementation = NearbyLivreurDto.class))),
            @ApiResponse(responseCode = "400", description = "Paramètres invalides", content = @Content)
    })
    @GetMapping("/nearby")
    public ResponseEntity<List<NearbyLivreurDto>> getNearbyLivreurs(
            @Parameter(description = "Latitude du point de départ")
            @RequestParam @NotNull @DecimalMin("-90.0") @DecimalMax("90.0") Double latitude,

            @Parameter(description = "Longitude du point de départ")
            @RequestParam @NotNull @DecimalMin("-180.0") @DecimalMax("180.0") Double longitude,

            @Parameter(description = "Rayon de recherche en kilomètres (défaut : 5 km)")
            @RequestParam(defaultValue = "5.0") @Positive Double radiusKm
    ) {
        log.info("event.action=GET_NEARBY_LIVREURS_REQUEST, event.outcome=RECEIVED, lat={}, lng={}, radiusKm={}",
                latitude, longitude, radiusKm);

        List<LivreurPosition> positions = getNearbyLivreursUseCase.getNearbyLivreurs(latitude, longitude, radiusKm);
        List<NearbyLivreurDto> dtos = positions.stream()
                .map(LivreurPositionMapper::toDto)
                .toList();

        log.info("event.action=GET_NEARBY_LIVREURS_REQUEST, event.outcome=SUCCESS, count={}", dtos.size());
        return ResponseEntity.ok(dtos);
    }
}