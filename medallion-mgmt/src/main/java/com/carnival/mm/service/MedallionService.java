package com.carnival.mm.service;

import com.carnival.mm.domain.Medallion;
import com.carnival.mm.repository.MedallionRepository;
import com.couchbase.client.protocol.views.ComplexKey;
import com.couchbase.client.protocol.views.Query;
import com.google.common.collect.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.List;

/**
 * Created by david.c.hoak on 6/22/2016.
 */
@Component("medallionService")
public class MedallionService {

    @Autowired
    private MedallionRepository medallionRepository;

    /**
     * Retrieves all Medallions from datastore
     *
     * @return
     */
    public List<Medallion> getAllMedallions() {

        return Lists.newArrayList(medallionRepository.retrieveAll());
    }

    /**
     * Retrieve Medallions from the datastore first and/or last name
     *
     * @param firstName
     * @param lastName
     * @return
     */
    public List<Medallion> searchMedallionsByName(String firstName, String lastName) {
        Query query = new Query();
        if (StringUtils.isEmpty(firstName)) {
            query.setKey(lastName.toLowerCase());
        } else {
            query.setKey(ComplexKey.of(firstName.toLowerCase(), lastName.toLowerCase()));
        }

        return medallionRepository.findByName(query);
    }

    /**
     * Retrieve Medallions from the datastore by the locatorId on the reservation
     *
     * @param locatorId
     * @return
     */
    public List<Medallion> searchMedallionsByLocatorId(String locatorId) {
        Query query = new Query();
        query.setKey(locatorId.toLowerCase());
        return medallionRepository.findByLocatorId(query);
    }

    /**
     * Find Medallion from the datastore by hardwareId
     *
     * @param hardwareId
     * @return
     */
    public Medallion findMedallionByHardwareId(String hardwareId) {
        Query query = new Query();
        query.setKey(hardwareId);
        return medallionRepository.findByHardwareId(query);
    }

    /**
     * Creates a new instance of the Medallion in the datastore
     *
     * @param medallion
     * @return
     */
    public Medallion createMedallion(Medallion medallion) {
        return medallionRepository.save(medallion);
    }
}
