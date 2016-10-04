package com.carnival.mm.domain;

import org.springframework.data.annotation.Id;
import org.springframework.data.couchbase.core.mapping.Document;
import org.springframework.data.couchbase.core.mapping.Field;

import java.util.Date;

/**
 * Created by d.jayaramaiah on 03-10-2016.
 */
@Document
public class GuestOrder {
    @Id
    private String id;
    @Field
    private String voyageId;
    @Field
    private String guestId;
    @Field
    private String firstName;
    @Field
    private String lastName;
    @Field
    private ShippingAddress shippingAddress;
    @Field
    private String voyageDate;
    @Field
    private String reservationId;
    @Field
    private String productCategory;
    @Field
    private Date created;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getVoyageId() {
        return voyageId;
    }

    public void setVoyageId(String voyageId) {
        this.voyageId = voyageId;
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

    public ShippingAddress getShippingAddress() {
        return shippingAddress;
    }

    public void setShippingAddress(ShippingAddress shippingAddress) {
        this.shippingAddress = shippingAddress;
    }

    public String getVoyageDate() {
        return voyageDate;
    }

    public void setVoyageDate(String voyageDate) {
        this.voyageDate = voyageDate;
    }

    public String getReservationId() {
        return reservationId;
    }

    public void setReservationId(String reservationId) {
        this.reservationId = reservationId;
    }

    public String getProductCategory() {
        return productCategory;
    }

    public void setProductCategory(String productCategory) {
        this.productCategory = productCategory;
    }

    public Date getCreated() {
        return created;
    }

    public void setCreated(Date created) {
        this.created = created;
    }

    public String toString(){
        return "voyageId :"+voyageId+" guestId :"+guestId+" firstName :"+firstName +" lastName :"+lastName+" shippingAddress :"+shippingAddress;
    }
}
