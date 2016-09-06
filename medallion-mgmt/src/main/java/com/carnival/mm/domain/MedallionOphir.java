package com.carnival.mm.domain;

import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.data.annotation.Id;
import org.springframework.data.couchbase.core.mapping.Document;
import org.springframework.data.couchbase.core.mapping.Field;

import javax.validation.constraints.NotNull;
import java.util.Date;

/**
 * Created by ps.r.balamurugan on 06-09-2016.
 */

@Document
public class MedallionOphir {

    @Id
    private String id;
    @Field @NotNull @NotEmpty
    private String hardwareId;
    @Field @NotNull @NotEmpty
    private String uuId;
    @Field
    private String idSKU;
    @Field
    private String idDisplayLocator;
    @Field
    private String firstName;
    @Field
    private String lastName;
    @Field
    private String shippingAddress;
    @Field
    private String voyageName;
    @Field
    private Date sailDate;
public MedallionOphir(){

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


    public String getUuId() {
        return uuId;
    }

    public void setUuId(String uuId) {
        this.uuId = uuId;
    }

    public String getIdSKU() {
        return idSKU;
    }

    public void setIdSKU(String idSKU) {
        this.idSKU = idSKU;
    }

    public String getIdDisplayLocator() {
        return idDisplayLocator;
    }

    public void setIdDisplayLocator(String idDisplayLocator) {
        this.idDisplayLocator = idDisplayLocator;
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

    public String getShippingAddress() {
        return shippingAddress;
    }

    public void setShippingAddress(String shippingAddress) {
        this.shippingAddress = shippingAddress;
    }

    public String getVoyageName() {
        return voyageName;
    }

    public void setVoyageName(String voyageName) {
        this.voyageName = voyageName;
    }

    public Date getSailDate() {
        return sailDate;
    }

    public void setSailDate(Date sailDate) {
        this.sailDate = sailDate;
    }
}
