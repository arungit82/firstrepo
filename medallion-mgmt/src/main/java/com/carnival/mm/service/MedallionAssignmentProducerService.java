package com.carnival.mm.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.messaging.Source;
import org.springframework.integration.support.MessageBuilder;
import org.springframework.stereotype.Component;

/**
 * Created by david on 7/26/16.
 */
@Component
public class MedallionAssignmentProducerService {

    private MedallionChannels channels;

    @Autowired
    public MedallionAssignmentProducerService(MedallionChannels channels) {
        this.channels = channels;
    }

    public void sayHello(String name) {
        channels.assignments().send(MessageBuilder.withPayload("Hello " + name).build());
    }
}
