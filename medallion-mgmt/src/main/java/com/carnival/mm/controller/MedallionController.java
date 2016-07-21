package com.carnival.mm.controller;

import com.carnival.mm.domain.Medallion;
import com.carnival.mm.service.MedallionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Created by david.c.hoak on 6/22/2016.
 */
@RestController
@RequestMapping("/medallionservice")
public class MedallionController {

    @Autowired
    MedallionService medallionService;

    public static final Logger log = LoggerFactory.getLogger(MedallionController.class);


    /**
     * Endpoint for accessing a single medallion by the given hardwareId
     *
     * @param hardwareId
     * @return
     */
    @RequestMapping(value = "/v1/medallion/{hardwareId}", method = RequestMethod.GET)
    public Medallion getMedallion(@PathVariable String hardwareId) {

        return medallionService.findMedallionByHardwareId(hardwareId);

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
    public List<Medallion> searchMedallionsByName(@RequestParam(value = "firstName", required = false) String firstName,
                                                  @RequestParam(value = "lastName", required = false) String lastName,
                                                  @RequestParam(value = "reservationId", required = false) String reservationId) {

        List<Medallion> medallions;
        if(StringUtils.isEmpty(reservationId)){
            medallions = medallionService.searchMedallionsByName(firstName, lastName);
        }
        else{
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
    public Medallion createMedallion(@RequestBody Medallion medallion) {

        return medallionService.createMedallion(medallion);

    }
}

