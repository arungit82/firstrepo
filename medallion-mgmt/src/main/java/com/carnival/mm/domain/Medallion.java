package com.carnival.mm.domain;

import org.springframework.data.annotation.Id;
import org.springframework.data.couchbase.core.mapping.Document;
import org.springframework.data.couchbase.core.mapping.Field;

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
    private String firstName;

    @Field
    private String lastName;

    /**
     * Constructor for conveniently creating a new Medallion with required fields
     * @param id
     * @param hardwareId
     * @param firstName
     * @param lastName
     */
    public Medallion(String id, String hardwareId, String firstName, String lastName) {
        this.id = id;
        this.hardwareId = hardwareId;
        this.firstName = firstName;
        this.lastName = lastName;
    }

    /**
     * Default Constructor
     */
    public Medallion(){
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
