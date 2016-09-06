package com.carnival.mm.controller;

import com.carnival.mm.domain.Medallion;
import com.carnival.mm.domain.MedallionOphir;
import com.carnival.mm.exception.MedallionNotFoundException;
import com.carnival.mm.service.MedallionOphirService;
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

/**
 * Created by ps.r.balamurugan on 06-09-2016.
 */


@RestController
@RequestMapping("/medallionserviceophir")
public class MedallionControllerOphir {

    @Autowired
    MedallionOphirService medallionOphirService;

    @Value("${ophir.filePath}")
    String filePath;

    public static final Logger log = LoggerFactory.getLogger(MedallionControllerOphir.class);

    /**
     * Endpoint for accessing a single medallion by the given uiid
     *
     * @param uuId
     * @return
     */
    @RequestMapping(value = "/v1/medallionophir/{uuId}", method = RequestMethod.GET)
    public MedallionOphir getMedallion(@PathVariable String uuId) {

        MedallionOphir medallionOphir = medallionOphirService.findMedallionByUuId(uuId);
        if (medallionOphir == null) {
            throw new MedallionNotFoundException(uuId);
        }
        else {

            Gson gson = new Gson();
            String jsonInString = gson.toJson(medallionOphir);

            Date dnow = new Date();
            SimpleDateFormat ft = new SimpleDateFormat("YYYYMMDD");
            String fileName = filePath+ft.format(dnow);
            FileWriter file = null;
            try {
                file = new FileWriter(fileName);
                file.write(jsonInString);

            }
            catch (IOException e){
                e.printStackTrace();
            }
            finally{
                try{
                file.flush();
                file.close();}
                catch(IOException ex){
                    ex.printStackTrace();
                }
            }
        }
        return medallionOphir;
    }

}
