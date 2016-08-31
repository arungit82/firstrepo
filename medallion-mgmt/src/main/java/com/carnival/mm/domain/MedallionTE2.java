package com.carnival.mm.domain;

import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.data.annotation.Id;
import org.springframework.data.couchbase.core.mapping.Document;
import org.springframework.data.couchbase.core.mapping.Field;

import javax.validation.constraints.NotNull;
import java.util.Date;

/**
 * Created by ps.r.balamurugan on 26-08-2016.
 */

public class MedallionTE2 {

    private String id;
     @NotNull @NotEmpty
    private String hardwareId;
     @NotNull @NotEmpty
    private String bleId;
     @NotNull @NotEmpty
    private String major;
     @NotNull @NotEmpty
    private String minor;
     @NotNull @NotEmpty
    private String uiid;
    @NotNull @NotEmpty
    private String uuid;
   @NotNull @NotEmpty
    private String nfcId;
     @NotNull @NotEmpty
    private String status;
      private String reservationId;
       private String _eventType;
        private String _version;
        private String _operation;
       private String _timestamp;

    /**
     * Default Constructor
     **/
    public MedallionTE2() {
        //required for data binding
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getHardwareId() {
        return hardwareId;
    }

    public void setHardwareId(String hardwareId) {
        this.hardwareId = hardwareId;
    }

     public String getBleId() {
        return bleId;
    }

    public void setBleId(String bleId) {
        this.bleId = bleId;
    }

    public String getMajor() {
        return major;
    }

    public void setMajor(String major) {
        this.major = major;
    }

    public String getMinor() {
        return minor;
    }

    public void setMinor(String minor) {
        this.minor = minor;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getUiid() {
        return uiid;
    }

    public void setUiid(String uiid) {
        this.uiid = uiid;
    }

    public String getNfcId() {
        return nfcId;
    }

    public void setNfcId(String nfcId) {
        this.nfcId = nfcId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getReservationId() {
        return reservationId;
    }

    public void setReservationId(String reservationId) {
        this.reservationId = reservationId;
    }

    public String get_eventType() {
        return _eventType;
    }

    public void set_eventType(String _eventType) {
        this._eventType = _eventType;
    }

    public String get_version() {
        return _version;
    }

    public void set_version(String _version) {
        this._version = _version;
    }

    public String get_operation() {
        return _operation;
    }

    public void set_operation(String _operation) {
        this._operation  =  _operation;
    }

    public String get_timestamp() {
        return _timestamp;
    }

    public void set_timestamp(String _timestamp) {
        this._timestamp = _timestamp;
    }
}
