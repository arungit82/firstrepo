package com.carnival.mm.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.integration.support.MessageBuilder;
import org.springframework.stereotype.Component;

/**
 * Created by david on 7/26/16.
 */
@Component
public class MedallionAssignmentTaskService {

    private MedallionChannels channels;

    @Autowired
    public MedallionAssignmentTaskService(MedallionChannels channels) {
        this.channels = channels;
    }

    /**
     * This is a proof of concept method for publishing a business event on kafka
     * @param name
     */
    public void sayHello(String name) {
        channels.assignments().send(MessageBuilder.withPayload("Hello " + name).build());
    }
}
