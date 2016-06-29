package com.carnival.mm.controller;

import com.carnival.mm.domain.Medallion;
import com.carnival.mm.service.MedallionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Created by david.c.hoak on 6/22/2016.
 */
@RestController
@RequestMapping("/medallion")
public class MedallionController {

    @Autowired
    MedallionService medallionService;

    public static final Logger log = LoggerFactory.getLogger(MedallionController.class);

    /**
     * Endpoint for accessing all medallions
     * @return
     */
    @RequestMapping(method = RequestMethod.GET)
    public List<Medallion> getAllMedallions(){

        return medallionService.getAllMedallions();
    }

    /**
     * Endpoint for accessing a single medallion by the given hardwareId
     * @param hardwareId
     * @return
     */
    @RequestMapping(value = "/{hardwareId}", method = RequestMethod.GET)
    public Medallion getMedallion(@PathVariable String hardwareId){

        return medallionService.getMedallionByHardwareId(hardwareId);

    }

    /**
     * Endpoint for creating a new medallion instance
     * @param medallion
     * @return
     */
    @RequestMapping(method = RequestMethod.POST)
    public Medallion createMedallion(@RequestBody Medallion medallion){

        return medallionService.createMedallion(medallion);

    }
}

