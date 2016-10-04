package com.carnival.mm.domain;

import java.io.Serializable;

/**
 * Created by d.jayaramaiah on 03-10-2016.
 */
public class ShippingAddress implements Serializable{

    private String addressId;
    private String firstName;
    private String lastName;
    private String organization;
    private String street1;
    private String street2;
    private String city;
    private String region;
    private String country;
    private String zipPostalCode;
    private String phoneNumber;

    public String getAddressId() {
        return addressId;
    }

    public void setAddressId(String addressId) {
        this.addressId = addressId;
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

    public String getOrganization() {
        return organization;
    }

    public void setOrganization(String organization) {
        this.organization = organization;
    }

    public String getStreet1() {
        return street1;
    }

    public void setStreet1(String street1) {
        this.street1 = street1;
    }

    public String getStreet2() {
        return street2;
    }

    public void setStreet2(String street2) {
        this.street2 = street2;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getZipPostalCode() {
        return zipPostalCode;
    }

    public void setZipPostalCode(String zipPostalCode) {
        this.zipPostalCode = zipPostalCode;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String toString(){
        return "addressId :"+addressId +" firstName :"+firstName+" lastName :"+lastName+" organization :"+organization+" street1 :"+street1
                + " street2 :"+street2+" city :"+city+" region :"+region+" country :"+country+" zipPostalCode :"+zipPostalCode+" phoneNumber :"+phoneNumber;
    }
}
