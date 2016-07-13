package com.carnival.mm.domain;

import org.springframework.data.annotation.Id;
import org.springframework.data.couchbase.core.mapping.Document;
import org.springframework.data.couchbase.core.mapping.Field;

import java.util.Date;

/**
 * Created by david.c.hoak on 6/22/2016.
 */
@Document
public class Medallion {

    @Id
    private String id;
    @Field
    private String hardwareId;
    @Field
    private String bleId;
    @Field
    private String majorId;
    @Field
    private String minorId;
    @Field
    private String nfcId;
    @Field
    private String uuId;
    @Field
    private String rssId;
    @Field
    private String batteryVolatageLevel;
    @Field
    private String state;

    @Field
    private String caseId;

    @Field
    private String guestId;
    @Field
    private String firstName;
    @Field
    private String lastName;
    @Field
    private String locatorId;
    @Field
    private String sku;
    @Field
    private String capColor;
    @Field
    private String ringColor;

    @Field
    private Date created;
    @Field
    private Date updated;
    @Field
    private String __type = "medallion";
    @Field
    private String __version = "1.0";

    /**
     * Default Constructor
     */
    public Medallion() {
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

    public String getMajorId() {
        return majorId;
    }

    public void setMajorId(String majorId) {
        this.majorId = majorId;
    }

    public String getMinorId() {
        return minorId;
    }

    public void setMinorId(String minorId) {
        this.minorId = minorId;
    }

    public String getNfcId() {
        return nfcId;
    }

    public void setNfcId(String nfcId) {
        this.nfcId = nfcId;
    }

    public String getUuId() {
        return uuId;
    }

    public void setUuId(String uuId) {
        this.uuId = uuId;
    }

    public String getRssId() {
        return rssId;
    }

    public void setRssId(String rssId) {
        this.rssId = rssId;
    }

    public String getBatteryVolatageLevel() {
        return batteryVolatageLevel;
    }

    public void setBatteryVolatageLevel(String batteryVolatageLevel) {
        this.batteryVolatageLevel = batteryVolatageLevel;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getCaseId() {
        return caseId;
    }

    public void setCaseId(String caseId) {
        this.caseId = caseId;
    }

    public String getGuestId() {
        return guestId;
    }

    public void setGuestId(String guestId) {
        this.guestId = guestId;
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

    public String getLocatorId() {
        return locatorId;
    }

    public void setLocatorId(String locatorId) {
        this.locatorId = locatorId;
    }

    public String getSku() {
        return sku;
    }

    public void setSku(String sku) {
        this.sku = sku;
    }

    public String getCapColor() {
        return capColor;
    }

    public void setCapColor(String capColor) {
        this.capColor = capColor;
    }

    public String getRingColor() {
        return ringColor;
    }

    public void setRingColor(String ringColor) {
        this.ringColor = ringColor;
    }

    public Date getCreated() {
        return created;
    }

    public void setCreated(Date created) {
        this.created = created;
    }

    public Date getUpdated() {
        return updated;
    }

    public void setUpdated(Date updated) {
        this.updated = updated;
    }

    public String get__type() {
        return __type;
    }

    public void set__type(String __type) {
        this.__type = __type;
    }

    public String get__version() {
        return __version;
    }

    public void set__version(String __version) {
        this.__version = __version;
    }
}
