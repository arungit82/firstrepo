package com.carnival.mm.domain;

import com.esotericsoftware.kryo.NotNull;
import org.hibernate.validator.constraints.NotEmpty;

/**
 * Created by david on 8/2/16.
 */
public class MedallionAssignment {

    @NotEmpty @NotNull
    private String hardwareId;
    @NotEmpty @NotNull
    private String uiid;
    private String firstName;
    private String lastName;

    public String getHardwareId() {
        return hardwareId;
    }

    public void setHardwareId(String hardwareId) {
        this.hardwareId = hardwareId;
    }

    public String getUiid() {
        return uiid;
    }

    public void setUiid(String uiid) {
        this.uiid = uiid;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }
}
