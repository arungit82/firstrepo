package com.carnival.mm.controller;

/**
 * Created by anbarasan.vincent on 9/12/2016.
 */

import com.carnival.mm.domain.Medallion;
import com.carnival.mm.domain.MedallionOphir;
import com.carnival.mm.service.MedallionService;
import com.google.gson.Gson;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;


@RestController
@RequestMapping("/medallionservice")
public class MedallionControllerOphir {

    public static final Logger log = LoggerFactory.getLogger(MedallionControllerOphir.class);
    @Autowired
    MedallionService medallionService;
    @Value("${ophir.filePath}")
    String filePath;

    /**
     * Endpoint for accessing a single medallion by the given uiid
     *
     * @param uIId
     * @return
     */
    @RequestMapping(value = "/v1/medallion/ophir/{uIId}", method = RequestMethod.GET)
    public void getMedallionsForShipping(@PathVariable String uIId) {

        MedallionOphir medallionOphir = null;
        Medallion tempMedallion = new Medallion();
        Date dnow = new Date();
        SimpleDateFormat ft = new SimpleDateFormat("YYYYMMDD");
        String fileName = filePath + ft.format(dnow) + ".txt";
        FileWriter file = null;

        List<Medallion> medallions = medallionService.findMedallionByGuestID(uIId);
        for (int i = 0; i < medallions.size(); i++) {
            tempMedallion = medallions.get(i);

            medallionOphir = new MedallionOphir();

            medallionOphir.setId(tempMedallion.getId());
            medallionOphir.setSKU("S16T-Med-RS");
            medallionOphir.setLocatorID("Locator_ID");
            medallionOphir.setFirstName(tempMedallion.getFirstName());
            medallionOphir.setLastName(tempMedallion.getLastName());
            medallionOphir.setShippingAddress("123 Main St, Towne_Miami, GL 99999");
            medallionOphir.setAccessorySelection("Accessary Selection");
            medallionOphir.setAccessoryMapping("Accessary Mapping");
            medallionOphir.setVoyageName("Voyage Name");
            medallionOphir.setuIId(tempMedallion.getGuestId());
            medallionOphir.setSailDate(new Date());
            medallionOphir.setGuestOrderID("Guest Order ID");

            Gson gson = new Gson();
            String jsonInString = gson.toJson(medallionOphir);

            try {
                file = new FileWriter(fileName, true);
                file.write(jsonInString);
                file.write(System.lineSeparator());
                file.flush();
                file.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}