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
@Document
public class MedallionTE2 {

    @Id
    private String id;
    @Field @NotNull @NotEmpty
    private String hardwareId;
    @Field @NotNull @NotEmpty
    private String bleId;
    @Field @NotNull @NotEmpty
    private String major;
    @Field @NotNull @NotEmpty
    private String minor;
    @Field @NotNull @NotEmpty
    private String uuid;
    @Field @NotNull @NotEmpty
    private String nfcId;
    @Field @NotNull @NotEmpty
    private String status;
    @Field
    private String reservationId;
    @Field
    private String _eventType;
    @Field
    private String _version;
    @Field
    private String _operation;
    @Field
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
