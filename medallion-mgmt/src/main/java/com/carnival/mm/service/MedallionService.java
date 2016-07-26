package com.carnival.mm.service;

import com.carnival.mm.domain.Medallion;
import com.carnival.mm.exception.MedallionAlreadyExistsException;
import com.carnival.mm.exception.MedallionNotFoundException;
import com.carnival.mm.repository.MedallionRepository;
import com.couchbase.client.protocol.views.ComplexKey;
import com.couchbase.client.protocol.views.Query;
import com.couchbase.client.protocol.views.Stale;
import com.google.common.collect.Lists;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 * Created by david.c.hoak on 6/22/2016.
 */
@Component("medallionService")
public class MedallionService {

    @Autowired
    private MedallionRepository medallionRepository;

    @Autowired
    private MedallionAssignmentProducerService producer;

    private static Logger log = LoggerFactory.getLogger(MedallionService.class);

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

        List<Medallion> medallions = medallionRepository.findByName(query);
        if(medallions.isEmpty()){throw new MedallionNotFoundException(firstName + " " + lastName);}

        return medallions;
    }

    /**
     * Retrieve Medallions from the datastore by the reservationId on the reservation
     *
     * @param reservationId
     * @return
     */
    public List<Medallion> searchMedallionsByReservationId(String reservationId) {
        Query query = new Query();
        query.setKey(reservationId.toLowerCase());

        List<Medallion> medallions = medallionRepository.findByReservationId(query);
        if(medallions.isEmpty()){throw new MedallionNotFoundException(reservationId);}

        return medallions;
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
        query.setStale(Stale.FALSE);

        Medallion medallion = medallionRepository.findByHardwareId(query);
        if(medallion == null){throw new MedallionNotFoundException(hardwareId);}

        producer.sayHello(hardwareId);
        return medallion;
    }

    /**
     * Creates a new instance of the Medallion in the datastore
     *
     * @param medallion
     * @return
     */
    public Medallion saveMedallion(Medallion medallion) {

        //Check if the id exists, indicating this is an update
        if(medallion.getId() != null && !medallion.getId().isEmpty()){
            if(!medallionRepository.exists(medallion.getId())){
                //throw exception
                throw new MedallionNotFoundException(medallion.getId());
            }
        }
        else{
            medallion.setId(UUID.randomUUID().toString());
            medallion.setCreated(new Date());
        }

        //Cannot save a medallion with a hardwareId that already exists
        try {
            Medallion existingMedallion = findMedallionByHardwareId(medallion.getHardwareId());
            if(existingMedallion != null){
                //throw exception
                throw new MedallionAlreadyExistsException(medallion.getHardwareId());
            }
        }
        catch(MedallionNotFoundException e){

            log.info("Medallion hardwareId not found... Inserting new medallion.");
        }

        medallion.setUpdated(new Date());
        return medallionRepository.save(medallion);
    }
}
