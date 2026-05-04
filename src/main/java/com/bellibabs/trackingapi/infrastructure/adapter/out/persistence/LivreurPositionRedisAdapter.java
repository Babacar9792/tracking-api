package com.bellibabs.trackingapi.infrastructure.adapter.out.persistence;

import com.bellibabs.trackingapi.domain.model.LivreurPosition;
import com.bellibabs.trackingapi.domain.port.out.LivreurPositionRepositoryPort;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.geo.Circle;
import org.springframework.data.geo.Distance;
import org.springframework.data.geo.GeoResult;
import org.springframework.data.geo.GeoResults;
import org.springframework.data.geo.Metrics;
import org.springframework.data.geo.Point;
import org.springframework.data.redis.connection.RedisGeoCommands;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.Collections;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class LivreurPositionRedisAdapter implements LivreurPositionRepositoryPort {

    private static final String GEO_KEY = "livreurs:positions";

    private final StringRedisTemplate redisTemplate;

    @Override
    public void save(LivreurPosition position) {
        redisTemplate.opsForGeo().add(
                GEO_KEY,
                new Point(position.getLongitude(), position.getLatitude()),
                position.getLivreurId()
        );
        log.info("event.action=SAVE_LIVREUR_POSITION, event.outcome=SUCCESS, livreurId={}, lat={}, lng={}",
                position.getLivreurId(), position.getLatitude(), position.getLongitude());
    }

    @Override
    public List<LivreurPosition> findNearby(Double latitude, Double longitude, Double radiusKm) {
        Circle circle = new Circle(
                new Point(longitude, latitude),
                new Distance(radiusKm, Metrics.KILOMETERS)
        );

        RedisGeoCommands.GeoRadiusCommandArgs args = RedisGeoCommands.GeoRadiusCommandArgs
                .newGeoRadiusArgs()
                .includeCoordinates()
                .includeDistance()
                .sortAscending();

        GeoResults<RedisGeoCommands.GeoLocation<String>> results =
                redisTemplate.opsForGeo().radius(GEO_KEY, circle, args);

        if (results == null) {
            return Collections.emptyList();
        }

        return results.getContent().stream()
                .map(this::toPosition)
                .toList();
    }

    private LivreurPosition toPosition(GeoResult<RedisGeoCommands.GeoLocation<String>> result) {
        LivreurPosition pos = new LivreurPosition();
        pos.setLivreurId(result.getContent().getName());
        pos.setLongitude(result.getContent().getPoint().getX());
        pos.setLatitude(result.getContent().getPoint().getY());
        pos.setDistanceKm(result.getDistance().getValue());
        pos.setUpdatedAt(Instant.now());
        return pos;
    }
}