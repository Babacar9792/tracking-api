package com.bellibabs.trackingapi.infrastructure.adapter.in.web;

import com.bellibabs.trackingapi.domain.port.in.SendPositionUseCase;
import com.bellibabs.trackingapi.infrastructure.adapter.in.request.SendPositionRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.stereotype.Controller;

@Slf4j
@Controller
@RequiredArgsConstructor
public class TrajetWebSocketController {

    private final SendPositionUseCase sendPositionUseCase;

    @MessageMapping("/position")
    public void handlePosition(SendPositionRequest request) {
        log.info("event.action=WS_POSITION_RECEIVED, event.outcome=STARTED, trajetId={}, lat={}, lng={}",
                request.getTrajetId(), request.getLatitude(), request.getLongitude());
        sendPositionUseCase.sendPosition(request.getTrajetId(), request.getLatitude(), request.getLongitude());
    }
}
