package com.bellibabs.trackingapi.domain.port.out;

import com.bellibabs.trackingapi.domain.model.PositionEvent;

public interface PositionPublisherPort {
    void publish(PositionEvent event);
}
