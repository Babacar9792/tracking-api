package com.bellibabs.trackingapi.infrastructure.adapter.in.web;

import com.bellibabs.trackingapi.domain.model.PositionEvent;
import com.bellibabs.trackingapi.domain.model.Trajet;
import com.bellibabs.trackingapi.domain.port.in.CreateTrajetUseCase;
import com.bellibabs.trackingapi.domain.port.in.GetTrajetByShareTokenUseCase;
import com.bellibabs.trackingapi.domain.port.in.GetTrajetHistoryUseCase;
import com.bellibabs.trackingapi.infrastructure.adapter.in.dto.PositionEventDto;
import com.bellibabs.trackingapi.infrastructure.adapter.in.dto.TrajetDto;
import com.bellibabs.trackingapi.infrastructure.adapter.in.request.CreateTrajetRequest;
import com.bellibabs.trackingapi.infrastructure.adapter.out.persistence.mapper.PositionEventMapper;
import com.bellibabs.trackingapi.infrastructure.adapter.out.persistence.mapper.TrajetMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@Slf4j
@RestController
@RequestMapping("/api/trajets")
@RequiredArgsConstructor
@Tag(name = "Trajets", description = "Gestion et suivi des trajets GPS")
public class TrajetController {

    private final CreateTrajetUseCase createTrajetUseCase;
    private final GetTrajetByShareTokenUseCase getTrajetByShareTokenUseCase;
    private final GetTrajetHistoryUseCase getTrajetHistoryUseCase;

    @Value("${app.tracking.base-url:https://app.com}")
    private String trackingBaseUrl;

    @Operation(summary = "Créer un trajet", description = "Démarre un nouveau trajet et génère un lien de partage unique.")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Trajet créé",
                    content = @Content(schema = @Schema(implementation = TrajetDto.class))),
            @ApiResponse(responseCode = "400", description = "Données invalides", content = @Content)
    })
    @PostMapping
    public ResponseEntity<TrajetDto> createTrajet(@RequestBody @Valid CreateTrajetRequest request) {
        log.info("event.action=CREATE_TRAJET_REQUEST, event.outcome=RECEIVED, clientId={}", request.getClientId());
        Trajet trajet = createTrajetUseCase.createTrajet(request.getClientId());
        TrajetDto dto = TrajetMapper.toDto(trajet, trackingBaseUrl);
        log.info("event.action=CREATE_TRAJET_REQUEST, event.outcome=SUCCESS, trajetId={}", dto.getId());
        return ResponseEntity.status(HttpStatus.CREATED).body(dto);
    }

    @Operation(summary = "Consulter un trajet par shareToken", description = "Accès public à un trajet via son lien de partage.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Trajet trouvé",
                    content = @Content(schema = @Schema(implementation = TrajetDto.class))),
            @ApiResponse(responseCode = "400", description = "Token inconnu", content = @Content)
    })
    @GetMapping("/track/{shareToken}")
    public ResponseEntity<TrajetDto> getByShareToken(
            @Parameter(description = "Token de partage UUID") @PathVariable UUID shareToken) {
        log.info("event.action=GET_TRAJET_BY_TOKEN_REQUEST, event.outcome=RECEIVED, shareToken={}", shareToken);
        Trajet trajet = getTrajetByShareTokenUseCase.getTrajetByShareToken(shareToken);
        return ResponseEntity.ok(TrajetMapper.toDto(trajet, trackingBaseUrl));
    }

    @Operation(summary = "Historique des positions", description = "Retourne toutes les positions enregistrées pour un trajet, triées par timestamp ASC.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Historique récupéré",
                    content = @Content(schema = @Schema(implementation = PositionEventDto.class))),
            @ApiResponse(responseCode = "400", description = "Trajet inconnu", content = @Content)
    })
    @GetMapping("/{trajetId}/history")
    public ResponseEntity<List<PositionEventDto>> getHistory(
            @Parameter(description = "ID du trajet") @PathVariable UUID trajetId) {
        log.info("event.action=GET_HISTORY_REQUEST, event.outcome=RECEIVED, trajetId={}", trajetId);
        List<PositionEvent> history = getTrajetHistoryUseCase.getTrajetHistory(trajetId);
        return ResponseEntity.ok(history.stream().map(PositionEventMapper::toDto).toList());
    }
}
