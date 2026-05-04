package com.bellibabs.trackingapi.infrastructure.adapter.in.web;

import com.bellibabs.trackingapi.domain.port.in.UpdateLivreurPositionUseCase;
import com.bellibabs.trackingapi.infrastructure.adapter.in.request.UpdateLivreurPositionRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.stereotype.Controller;

@Slf4j
@Controller
@RequiredArgsConstructor
public class LivreurWebSocketController {

    private final UpdateLivreurPositionUseCase updateLivreurPositionUseCase;

    @MessageMapping("/livreur/position")
    public void handleLivreurPosition(UpdateLivreurPositionRequest request) {
        log.info("event.action=WS_LIVREUR_POSITION_RECEIVED, event.outcome=STARTED, livreurId={}, lat={}, lng={}",
                request.getLivreurId(), request.getLatitude(), request.getLongitude());
        updateLivreurPositionUseCase.updatePosition(
                request.getLivreurId(),
                request.getLatitude(),
                request.getLongitude()
        );
    }
}