package com.bellibabs.trackingapi.infrastructure.adapter.out.websocket;

import com.bellibabs.trackingapi.domain.model.PositionEvent;
import com.bellibabs.trackingapi.domain.port.out.PositionPublisherPort;
import com.bellibabs.trackingapi.infrastructure.adapter.in.dto.PositionEventDto;
import com.bellibabs.trackingapi.infrastructure.adapter.out.persistence.mapper.PositionEventMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class PositionPublisherAdapter implements PositionPublisherPort {

    private final SimpMessagingTemplate messagingTemplate;

    @Override
    public void publish(PositionEvent event) {
        String destination = "/topic/trajet/" + event.getTrajetId();
        PositionEventDto dto = PositionEventMapper.toDto(event);

        log.info("event.action=WS_PUBLISH_POSITION, event.outcome=STARTED, trajetId={}, destination={}",
                event.getTrajetId(), destination);

        messagingTemplate.convertAndSend(destination, dto);

        log.info("event.action=WS_PUBLISH_POSITION, event.outcome=SUCCESS, trajetId={}", event.getTrajetId());
    }
}
