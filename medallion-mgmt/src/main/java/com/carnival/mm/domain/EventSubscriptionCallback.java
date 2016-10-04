package com.carnival.mm.domain;

import com.esotericsoftware.kryo.NotNull;
import org.hibernate.validator.constraints.NotEmpty;

/**
 * Created by anbarasan.vincent on 10/4/2016.
 */

public class EventSubscriptionCallback {
    @NotEmpty
    @NotNull
    private String callbackURL;
    @NotEmpty @NotNull
    private String eventType;

    public String getCallbackURL() {
        return callbackURL;
    }

    public void setCallbackURL(String callbackURL) {
        this.callbackURL = callbackURL;
    }

    public String getEventType() {
        return eventType;
    }

    public void setEventType(String eventType) {
        this.eventType = eventType;
    }

}
