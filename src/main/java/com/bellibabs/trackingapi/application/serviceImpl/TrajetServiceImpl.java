package com.bellibabs.trackingapi.application.serviceImpl;

import com.bellibabs.trackingapi.domain.model.PositionEvent;
import com.bellibabs.trackingapi.domain.model.Trajet;
import com.bellibabs.trackingapi.domain.model.TrajetStatut;
import com.bellibabs.trackingapi.domain.port.in.CreateTrajetUseCase;
import com.bellibabs.trackingapi.domain.port.in.GetAllTrajetsUseCase;
import com.bellibabs.trackingapi.domain.port.in.GetTrajetByShareTokenUseCase;
import com.bellibabs.trackingapi.domain.port.in.GetTrajetHistoryUseCase;
import com.bellibabs.trackingapi.domain.port.in.SendPositionUseCase;
import com.bellibabs.trackingapi.domain.port.out.PositionPublisherPort;
import com.bellibabs.trackingapi.domain.port.out.PositionRepositoryPort;
import com.bellibabs.trackingapi.domain.port.out.TrajetRepositoryPort;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class TrajetServiceImpl implements CreateTrajetUseCase, SendPositionUseCase,
        GetTrajetByShareTokenUseCase, GetTrajetHistoryUseCase, GetAllTrajetsUseCase {

    private final TrajetRepositoryPort trajetRepositoryPort;
    private final PositionRepositoryPort positionRepositoryPort;
    private final PositionPublisherPort positionPublisherPort;

    @Override
    public Trajet createTrajet(String clientId) {
        String traceId = MDC.get("traceId");
        log.info("event.action=CREATE_TRAJET, event.outcome=STARTED, clientId={}, traceId={}", clientId, traceId);

        Trajet trajet = new Trajet();
        trajet.setId(UUID.randomUUID());
        trajet.setClientId(clientId);
        trajet.setStatut(TrajetStatut.STARTED);
        trajet.setShareToken(UUID.randomUUID());
        trajet.setCreatedAt(LocalDateTime.now());

        Trajet saved = trajetRepositoryPort.save(trajet);
        log.info("event.action=CREATE_TRAJET, event.outcome=SUCCESS, trajetId={}, shareToken={}, traceId={}",
                saved.getId(), saved.getShareToken(), traceId);
        return saved;
    }

    @Override
    public PositionEvent sendPosition(UUID trajetId, Double latitude, Double longitude) {
        String traceId = MDC.get("traceId");
        log.info("event.action=SEND_POSITION, event.outcome=STARTED, trajetId={}, traceId={}", trajetId, traceId);

        Trajet trajet = trajetRepositoryPort.findById(trajetId)
                .orElseThrow(() -> {
                    log.warn("event.action=SEND_POSITION, event.outcome=FAILURE, message=Trajet not found, trajetId={}", trajetId);
                    return new IllegalArgumentException("Trajet introuvable : " + trajetId);
                });

        if (trajet.getStatut() == TrajetStatut.STARTED) {
            trajet.setStatut(TrajetStatut.IN_PROGRESS);
            trajetRepositoryPort.save(trajet);
        }

        PositionEvent event = new PositionEvent();
        event.setId(UUID.randomUUID());
        event.setTrajetId(trajetId);
        event.setLatitude(latitude);
        event.setLongitude(longitude);
        event.setTimestamp(Instant.now());

        PositionEvent saved = positionRepositoryPort.save(event);
        positionPublisherPort.publish(saved);

        log.info("event.action=SEND_POSITION, event.outcome=SUCCESS, trajetId={}, lat={}, lng={}, traceId={}",
                trajetId, latitude, longitude, traceId);
        return saved;
    }

    @Override
    public Trajet getTrajetByShareToken(UUID shareToken) {
        log.info("event.action=GET_TRAJET_BY_TOKEN, event.outcome=STARTED, shareToken={}", shareToken);

        return trajetRepositoryPort.findByShareToken(shareToken)
                .orElseThrow(() -> {
                    log.warn("event.action=GET_TRAJET_BY_TOKEN, event.outcome=FAILURE, message=Trajet not found, shareToken={}", shareToken);
                    return new IllegalArgumentException("Trajet introuvable pour le token : " + shareToken);
                });
    }

    @Override
    public List<Trajet> getAllTrajets() {
        log.info("event.action=GET_ALL_TRAJETS, event.outcome=STARTED");
        List<Trajet> trajets = trajetRepositoryPort.findAll();
        log.info("event.action=GET_ALL_TRAJETS, event.outcome=SUCCESS, count={}", trajets.size());
        return trajets;
    }

    @Override
    public List<PositionEvent> getTrajetHistory(UUID trajetId) {
        log.info("event.action=GET_TRAJET_HISTORY, event.outcome=STARTED, trajetId={}", trajetId);

        trajetRepositoryPort.findById(trajetId)
                .orElseThrow(() -> new IllegalArgumentException("Trajet introuvable : " + trajetId));

        List<PositionEvent> history = positionRepositoryPort.findByTrajetIdOrderByTimestampAsc(trajetId);
        log.info("event.action=GET_TRAJET_HISTORY, event.outcome=SUCCESS, trajetId={}, count={}", trajetId, history.size());
        return history;
    }
}
