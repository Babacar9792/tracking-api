package com.bellibabs.trackingapi.application.serviceImpl;

import com.bellibabs.trackingapi.domain.model.LivreurPosition;
import com.bellibabs.trackingapi.domain.model.NearbyLivreursResult;
import com.bellibabs.trackingapi.domain.port.in.GetNearbyLivreursUseCase;
import com.bellibabs.trackingapi.domain.port.in.UpdateLivreurPositionUseCase;
import com.bellibabs.trackingapi.domain.port.out.LivreurPositionRepositoryPort;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class LivreurServiceImpl implements UpdateLivreurPositionUseCase, GetNearbyLivreursUseCase {

    private final LivreurPositionRepositoryPort livreurPositionRepositoryPort;

    @Value("${app.livreur.search.initial-radius-km:5.0}")
    private double initialRadiusKm;

    @Value("${app.livreur.search.max-radius-km:200.0}")
    private double maxRadiusKm;

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
    public NearbyLivreursResult getNearbyLivreurs(Double latitude, Double longitude) {
        String traceId = MDC.get("traceId");
        log.info("event.action=GET_NEARBY_LIVREURS, event.outcome=STARTED, lat={}, lng={}, initialRadiusKm={}, maxRadiusKm={}, traceId={}",
                latitude, longitude, initialRadiusKm, maxRadiusKm, traceId);

        double radius = initialRadiusKm;
        while (true) {
            List<LivreurPosition> result = livreurPositionRepositoryPort.findNearby(latitude, longitude, radius);
            if (!result.isEmpty() || radius >= maxRadiusKm) {
                log.info("event.action=GET_NEARBY_LIVREURS, event.outcome=SUCCESS, count={}, effectiveRadiusKm={}, traceId={}",
                        result.size(), radius, traceId);
                return new NearbyLivreursResult(result, radius);
            }
            double next = Math.min(radius * 2, maxRadiusKm);
            log.debug("event.action=GET_NEARBY_LIVREURS, event.outcome=EXPANDING, radiusKm={} -> {}, traceId={}",
                    radius, next, traceId);
            radius = next;
        }
    }
}