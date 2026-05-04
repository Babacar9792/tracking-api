package com.bellibabs.trackingapi.application.serviceImpl;

import com.bellibabs.trackingapi.domain.model.LivreurPosition;
import com.bellibabs.trackingapi.domain.port.in.GetNearbyLivreursUseCase;
import com.bellibabs.trackingapi.domain.port.in.UpdateLivreurPositionUseCase;
import com.bellibabs.trackingapi.domain.port.out.LivreurPositionRepositoryPort;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class LivreurServiceImpl implements UpdateLivreurPositionUseCase, GetNearbyLivreursUseCase {

    private final LivreurPositionRepositoryPort livreurPositionRepositoryPort;

    @Override
    public LivreurPosition updatePosition(String livreurId, Double latitude, Double longitude) {
        String traceId = MDC.get("traceId");
        log.info("event.action=UPDATE_LIVREUR_POSITION, event.outcome=STARTED, livreurId={}, traceId={}", livreurId, traceId);

        LivreurPosition position = new LivreurPosition(livreurId, latitude, longitude, Instant.now());
        livreurPositionRepositoryPort.save(position);

        log.info("event.action=UPDATE_LIVREUR_POSITION, event.outcome=SUCCESS, livreurId={}, lat={}, lng={}, traceId={}",
                livreurId, latitude, longitude, traceId);
        return position;
    }

    @Override
    public List<LivreurPosition> getNearbyLivreurs(Double latitude, Double longitude, Double radiusKm) {
        String traceId = MDC.get("traceId");
        log.info("event.action=GET_NEARBY_LIVREURS, event.outcome=STARTED, lat={}, lng={}, radiusKm={}, traceId={}",
                latitude, longitude, radiusKm, traceId);

        List<LivreurPosition> result = livreurPositionRepositoryPort.findNearby(latitude, longitude, radiusKm);

        log.info("event.action=GET_NEARBY_LIVREURS, event.outcome=SUCCESS, count={}, traceId={}", result.size(), traceId);
        return result;
    }
}