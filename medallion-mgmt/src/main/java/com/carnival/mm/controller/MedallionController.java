package com.carnival.mm.controller;

import com.carnival.mm.domain.Medallion;
import com.carnival.mm.domain.MedallionAssignment;
import com.carnival.mm.exception.MedallionCannotUpdateException;
import com.carnival.mm.exception.MedallionNotFoundException;
import com.carnival.mm.service.MedallionAssignmentTaskService;
import com.carnival.mm.service.MedallionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

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
     * Endpoint for assigning a medallion to an individual
     * @param medallionAssignment
     * @return
     */
    @RequestMapping(value="/v1/medallion-assignment", method=RequestMethod.POST)
    public Medallion assignMedallion(@Valid @RequestBody MedallionAssignment medallionAssignment){

        Medallion medallion = medallionAssignmentTaskService.assignMedallionToIndividual(medallionAssignment);
        return medallion;
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

    //Comments from Bala    

}

