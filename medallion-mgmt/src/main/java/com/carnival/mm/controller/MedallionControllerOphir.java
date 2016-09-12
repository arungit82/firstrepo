package com.carnival.mm.controller;

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

import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;
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
    @Value("${ophir.ftpURL}")
    String ftpURL;
    @Value("${ophir.host}")
    String host;
    @Value("${ophir.user}")
    String user;
    @Value("${ophir.pass}")
    String pass;
    @Value("${ophir.uploadPath}")
    String uploadPath;

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
            String medallionData = gson.toJson(medallionOphir);
            if (medallionData.contains(",")){
                medallionData = medallionData.replace(",", "|");
            }

            Date dnow = new Date();
            SimpleDateFormat ft = new SimpleDateFormat("YYYYMMDD");
            String fileName = filePath+ft.format(dnow);

            FileWriter file = null;
            try {
                file = new FileWriter(fileName);
                file.write(medallionData);
            }
            catch (IOException e){
                e.printStackTrace();
            }
            finally{
                try{
                    file.flush();
                    file.close();
                }
                catch(IOException ex){
                        ex.printStackTrace();
                }
            }
        }
        return medallionOphir;
    }

    public boolean uploadFileFTP(){

        boolean retValue = false;
        final int BUFFER_SIZE = 4096;
        ftpURL = String.format(ftpURL, user, pass, host, uploadPath);

        try {
            URL url = new URL(ftpURL);
            URLConnection conn = url.openConnection();
            OutputStream outputStream = conn.getOutputStream();
            FileInputStream inputStream = new FileInputStream(filePath);

            byte[] buffer = new byte[BUFFER_SIZE];
            int bytesRead = -1;
            while ((bytesRead = inputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, bytesRead);
            }

            inputStream.close();
            outputStream.close();

            retValue = true;
            System.out.println("File uploaded");
        }
        catch (IOException ex) {
            ex.printStackTrace();
        }
        return retValue;
    }

}
