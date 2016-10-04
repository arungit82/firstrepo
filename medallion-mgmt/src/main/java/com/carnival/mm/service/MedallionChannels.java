package com.carnival.mm.service;

import org.springframework.cloud.stream.annotation.Input;
import org.springframework.cloud.stream.annotation.Output;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.SubscribableChannel;

/**
 * Created by david on 7/26/16.
 */
public interface MedallionChannels {
    @Output("medallionAssignment")
    MessageChannel assignments();

    @Input("reservation")
    SubscribableChannel reservations();
}
