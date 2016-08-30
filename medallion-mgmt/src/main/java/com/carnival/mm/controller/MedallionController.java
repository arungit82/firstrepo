package com.carnival.mm.controller;

import com.carnival.mm.domain.Medallion;
import com.carnival.mm.domain.MedallionAssignment;
import com.carnival.mm.domain.MedallionTE2;
import com.carnival.mm.exception.MedallionCannotUpdateException;
import com.carnival.mm.exception.MedallionNotFoundException;
import com.carnival.mm.service.MedallionAssignmentTaskService;

import com.carnival.mm.service.MedallionService;
import com.carnival.mm.service.MedallionTE2Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Date;
/**
 * Created by david.c.hoak on 6/22/2016.
 */
@RestController
@RequestMapping("/medallionservice")
public class MedallionController {

    @Autowired
    MedallionService medallionService;

    @Autowired
    MedallionAssignmentTaskService medallionAssignmentTaskService;

    @Autowired
    MedallionTE2Service medallionTE2Service;


    public static final Logger log = LoggerFactory.getLogger(MedallionController.class);


    /**
     * Endpoint for accessing a single medallion by the given hardwareId
     *
     * @param hardwareId
     * @return
     */
    @RequestMapping(value = "/v1/medallion/{hardwareId}", method = RequestMethod.GET)
    public Medallion getMedallion(@PathVariable String hardwareId) {

        Medallion medallion = medallionService.findMedallionByHardwareId(hardwareId);
        if (medallion == null) {
            throw new MedallionNotFoundException(hardwareId);
        }
        return medallion;
    }

    /**
     * Endpoint for updating a single medallion by the given hardwareId
     *
     * @param hardwareId
     * @return
     */
    @RequestMapping(value = "/v1/medallion/{hardwareId}", method = RequestMethod.POST)
    public Medallion updateMedallion(@PathVariable String hardwareId, @Valid @RequestBody Medallion medallion) {

        if (!medallion.getHardwareId().equals(hardwareId)) {
            throw new MedallionCannotUpdateException(hardwareId);
        }
        return medallionService.updateMedallion(medallion);
    }

    /**
     * Endpoint for accessing a single medallion by the given hardwareId
     *
     * @param firstName
     * @param lastName
     * @param reservationId
     * @return
     */
    @RequestMapping(value = "/v1/medallions", method = RequestMethod.GET)
    public List<Medallion> searchMedallions(@RequestParam(value = "firstName", required = false) String firstName,
                                                  @RequestParam(value = "lastName", required = false) String lastName,
                                                  @RequestParam(value = "reservationId", required = false) String reservationId) {

        List<Medallion> medallions;
        if (StringUtils.isEmpty(reservationId)) {
            medallions = medallionService.searchMedallionsByName(firstName, lastName);
        } else {
            medallions = medallionService.searchMedallionsByReservationId(reservationId);
        }
        return medallions;
    }

    /**
     * Endpoint for creating a new medallion instance
     *
     * @param medallion
     * @return
     */
    @RequestMapping(value = "/v1/medallion", method = RequestMethod.POST)
    public Medallion createMedallion(@Valid @RequestBody Medallion medallion) {

        return medallionService.createMedallion(medallion);

    }

    /**
     * Endpoint for creating a new medallion instance
     *
     * @param medallionTE2
     * @return
     */
    @RequestMapping(value = "/v1/medallion-callback", method = RequestMethod.POST)
    public void updateMedallionAssignment(@Valid @RequestBody MedallionTE2 medallionTE2) {


        // To do: Call back assignment
        // return medallionService.createMedallion(medallion);


    }


    /**
     * Endpoint for assigning a medallion to an individual
     * @param medallionAssignment
     * @return
     */
    @RequestMapping(value="/v1/medallion-assignment", method=RequestMethod.POST)
    public Medallion assignMedallion(@Valid @RequestBody MedallionAssignment medallionAssignment){

        Medallion medallion = medallionAssignmentTaskService.assignMedallionToIndividual(medallionAssignment);
        MedallionTE2 tempMedallionTE2 = getMedallionTE2(medallion);
        //call --- https://dev-trident.te2.biz/rest/v1/event-subscriptions/event
        return medallion;
    }

    /**
     * Endpoint for getting medallion using Guest ID
     * @param guestId
     * @return
     */
    @RequestMapping(value="/v1/medallion-GUID/{guestId}", method=RequestMethod.GET)
     public Medallion getMedallionGuestID(@PathVariable String guestId){
        Medallion medallion = medallionService.findMedallionByGuestID(guestId);
        if (medallion == null) {
            throw new MedallionNotFoundException(guestId);
        }
        return medallion;

    }

     private MedallionTE2 getMedallionTE2(Medallion medallion) {

        MedallionTE2 medallionTE2 = new MedallionTE2();
       // Medallion medallion = medallionService.findMedallionByHardwareId(hardwareId);

        if (medallion == null) {
            //throw new MedallionNotFoundException();
        }
        else{
            medallionTE2.setId(medallion.getId());
            medallionTE2.setHardwareId(medallion.getHardwareId());
            medallionTE2.setUiId(medallion.getUuId());
            medallionTE2.setBleId(medallion.getBleId());
            medallionTE2.setMajorId(medallion.getMajorId());
            medallionTE2.setMinorId(medallion.getMinorId());
            medallionTE2.setUuid(medallion.getUuId());
            medallionTE2.setNfcId(medallion.getNfcId());
            medallionTE2.setStatus(medallion.getStatus());
            medallionTE2.setReservationId(medallion.getReservationId());
            medallionTE2.set_eventType("MedallionAssignment");
            medallionTE2.set_operation("create");
            medallionTE2.set_version("1.0");
            Date dNow = new Date( );
            SimpleDateFormat ft = new SimpleDateFormat ("yyyy.MM.dd hh:mm:ss");
            medallionTE2.set_timestamp(ft.format(dNow));
        }
        return medallionTE2;
    }

     /**
     * Endpoint for getting the count of unassigned medallions.  This is a throw away method only to be used for
     * purposes of the release 9 experience slice demo.
     * @return
     */
    @RequestMapping(value="/v1/medallion-count", method=RequestMethod.GET)
    public int medallionUnassignedCount(){
        return medallionService.getAvailableMedallionCount();
    }



}

