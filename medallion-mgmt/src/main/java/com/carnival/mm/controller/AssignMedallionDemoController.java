package com.carnival.mm.controller;

import com.carnival.mm.domain.Medallion;
import com.carnival.mm.service.MedallionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;

/**
 * Created by david.c.hoak on 6/22/2016.
 */
@Controller
@RequestMapping("/medallion")
public class AssignMedallionDemoController {

    @Autowired
    MedallionService medallionService;

    public static final Logger log = LoggerFactory.getLogger(AssignMedallionDemoController.class);

    /**
     * Endpoint for creating a new medallion instance
     *
     * @param medallion
     * @return
     */
    @RequestMapping(value = "/assign", method = RequestMethod.POST)
    public Medallion assignMedallion(@Valid @RequestBody Medallion medallion) {

        return medallionService.createMedallion(medallion);

    }

    @RequestMapping(value = "/demo", method = RequestMethod.GET)
    public ModelAndView demo() {

        ModelAndView mv = new ModelAndView("demo");
        int count = medallionService.getAvailableMedallionCount();
        mv.addObject("inventory", count);

        return mv;

    }
}

