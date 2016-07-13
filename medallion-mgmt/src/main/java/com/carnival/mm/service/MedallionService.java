package com.carnival.mm.service;

import com.carnival.mm.domain.Medallion;

import java.util.List;

/**
 * Created by david.c.hoak on 6/22/2016.
 */
public interface MedallionService {

    /**
     * Retrieves all Medallions from datastore
     * @return
     */
    List<Medallion> getAllMedallions();

    /**
     * Retrieve Medallions from the datastore first and/or last name
     * @param firstName
     * @param lastName
     * @return
     */
    List<Medallion> searchMedallionsByName(String firstName, String lastName);

    /**
     * Retrieve Medallions from the datastore by the locatorId on the reservation
     * @param locatorId
     * @return
     */
    List<Medallion> searchMedallionsByLocatorId(String locatorId);

    /**
     * Find Medallion from the datastore by hardwareId
     * @param hardwareId
     * @return
     */
    Medallion findMedallionByHardwareId(String hardwareId);

    /**
     * Creates a new instance of the Medallion in the datastore
     * @param medallion
     * @return
     */
    Medallion createMedallion(Medallion medallion);
}
