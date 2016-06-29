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
     * Retrieves Medallion from datastore based on given hardwareId
     * @param hardwareId
     * @return
     */
    Medallion getMedallionByHardwareId(String hardwareId);

    /**
     * Creates a new instance of the Medallion in the datastore
     * @param medallion
     * @return
     */
    Medallion createMedallion(Medallion medallion);
}
