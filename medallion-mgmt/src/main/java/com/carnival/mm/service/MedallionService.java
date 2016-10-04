package com.carnival.mm.service;

import com.carnival.mm.domain.GuestOrder;
import com.carnival.mm.domain.Medallion;
import com.carnival.mm.domain.MedallionStatus;
import com.carnival.mm.domain.MedallionAssignEventPublish;
import com.carnival.mm.exception.GuestOrderCannotUpdateException;
import com.carnival.mm.exception.MedallionAlreadyExistsException;
import com.carnival.mm.exception.MedallionCannotUpdateException;
import com.carnival.mm.exception.MedallionNotFoundException;
import com.carnival.mm.repository.GuestOrderRepository;
import com.carnival.mm.repository.MedallionRepository;
import com.couchbase.client.protocol.views.ComplexKey;
import com.couchbase.client.protocol.views.Query;
import com.couchbase.client.protocol.views.Stale;
import com.google.common.collect.Lists;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 * Created by david.c.hoak on 6/22/2016.
 */
@Service("medallionService")
public class MedallionService {

    @Autowired
    private MedallionRepository medallionRepository;

    @Autowired
    private GuestOrderRepository guestOrderRepository;

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

        return medallionRepository.findByHardwareId(query);
    }

    /**
     * Find Medallion from the datastore by hardwareId
     *
     * @param hardwareId
     * @return
     */
    public MedallionAssignEventPublish findMedallionByHardwareIdAssignEventPublish(String hardwareId) {
        Query query = new Query();
        query.setKey(hardwareId);
        query.setStale(Stale.FALSE);

        return medallionRepository.findByHardwareIdTAssignEventPublish(query);
    }

    /**
     * Find Medallion from the datastore by guestId
     *
     * @param guestId
     * @return
     */
    public List<Medallion> findMedallionByGuestID(String guestId) {

        String strGuestId = "\"" +guestId+ "\"";

        Query query = new Query();
        query.setKey(strGuestId);
        query.setStale(Stale.FALSE);

        //return medallionRepository.findByGuestId(query);
        List<Medallion> medallions = medallionRepository.findByGuestId(query);
        if(medallions.isEmpty()){throw new MedallionNotFoundException(guestId);}

        return medallions;

    }

    public Medallion updateMedallion(Medallion updatedMedallion){

        if(!medallionRepository.exists(updatedMedallion.getId())){
            //throw exception
            throw new MedallionNotFoundException(updatedMedallion.getId());
        }
        Medallion medallion = findMedallionByHardwareId(updatedMedallion.getHardwareId());
        //Update current Medallion
        medallion.setDocument_type("history");
        medallionRepository.save(medallion);

        //Create new Medallion
        BeanUtils.copyProperties(updatedMedallion, medallion);
        updatedMedallion.setUpdated(new Date());

        //Incrementing current version
        updatedMedallion.setId(UUID.randomUUID().toString());
        updatedMedallion.setDocument_type("current");
        updatedMedallion.set__version((Float.parseFloat(updatedMedallion.get__version())+1)+"");

        return medallionRepository.save(updatedMedallion);
        //return updatedMedallion;
    }

    /**
     * Creates a new instance of the Medallion in the datastore
     *
     * @param medallion
     * @return
     */
    public Medallion createMedallion(Medallion medallion) {

        //Check if the id exists, indicating that an update is being attempted
        if(medallion.getId() != null && !medallion.getId().isEmpty()){
                //throw exception
                throw new MedallionCannotUpdateException(medallion.getId());
        }

        //Cannot save a medallion with a hardwareId that already exists
        Medallion existingMedallion = findMedallionByHardwareId(medallion.getHardwareId());
        if(existingMedallion != null){
            //throw exception
            throw new MedallionAlreadyExistsException(medallion.getHardwareId());
        }

        medallion.setId(UUID.randomUUID().toString());
        medallion.setCreated(new Date());
        medallion.setUpdated(new Date());

        return medallionRepository.save(medallion);
    }




    /**
     * Returns the count of medallions in a UNASSIGNED status
     * @return
     */
    public int getAvailableMedallionCount(){

        Query query = new Query();
        query.setKey(MedallionStatus.UNASSIGNED.toString());
        query.setStale(Stale.FALSE);

        return medallionRepository.findByStatus(query).size();
    }

    /**
     * Find Medallion History from the datastore by hardwareId
     *
     * @param hardwareId
     * @return
     */
    public List<Medallion> findMedallionHistoryByHardwareId(String hardwareId) {
        Query query = new Query();
        query.setKey(hardwareId);
        query.setStale(Stale.FALSE);
        query.setDescending(true);

        return medallionRepository.findByVersion(query);
    }

    /**
     * Creates a new instance of the Guest Order in the datastore
     *
     * @param order
     * @return
     */
    public GuestOrder createGuestOrder(GuestOrder order) {

        //Check if the id exists, indicating that an update is being attempted
        if(order.getId() != null && !order.getId().isEmpty()){
            //throw exception
            throw new GuestOrderCannotUpdateException(order.getId());
        }

        order.setId(UUID.randomUUID().toString());
        order.setCreated(new Date());

        return guestOrderRepository.save(order);
    }
}
