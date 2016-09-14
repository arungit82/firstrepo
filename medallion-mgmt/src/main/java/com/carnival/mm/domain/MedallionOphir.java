package com.carnival.mm.domain;

/**
 * Created by anbarasan.vincent on 9/12/2016.
 */
        import org.hibernate.validator.constraints.NotEmpty;
        import org.springframework.data.annotation.Id;
        import org.springframework.data.couchbase.core.mapping.Document;
        import org.springframework.data.couchbase.core.mapping.Field;

        import javax.validation.constraints.NotNull;
        import java.util.Date;


@Document
public class MedallionOphir {

    @Id
    private String id;
    @Field @NotNull @NotEmpty
    private String SKU;
    @Field @NotNull @NotEmpty
    private String locatorID;
    @Field
    private String firstName;
    @Field
    private String lastName;
    @Field
    private String shippingAddress;
    @Field
    private String accessorySelection;
    @Field
    private String accessoryMapping;
    @Field
    private String voyageName;
    @Field
    private String uIId;
    @Field
    private Date sailDate;
    private String guestOrderID;
    @Field


    public MedallionOphir(){

    }
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSKU() {
        return SKU;
    }

    public void setSKU(String SKU) {
        this.SKU = SKU;
    }


    public String getLocatorID() {
        return locatorID;
    }

    public void setLocatorID(String locatorID) {
        this.locatorID = locatorID;
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

    public String getAccessorySelection() {
        return accessorySelection;
    }

    public void setAccessorySelection(String accessorySelection) {
        this.accessorySelection = accessorySelection;
    }

    public String getAccessoryMapping() {
        return accessoryMapping;
    }

    public void setAccessoryMapping(String accessoryMapping) {
        this.accessoryMapping = accessoryMapping;
    }

    public String getVoyageName() {
        return voyageName;
    }

    public void setVoyageName(String voyageName) {
        this.voyageName = voyageName;
    }

    public String getuIId() {
        return uIId;
    }

    public void setuIId(String uIId) {
        this.uIId = uIId;
    }

    public Date getSailDate() {
        return sailDate;
    }

    public void setSailDate(Date sailDate) {
        this.sailDate = sailDate;
    }

    public String getGuestOrderID() {
        return guestOrderID;
    }

    public void setGuestOrderID(String guestOrderID) {
        this.guestOrderID = guestOrderID;
    }
}