package com.carnival.mm.controller;

import java.io.BufferedReader;
import java.io.IOException;

import com.carnival.mm.domain.Medallion;
import com.carnival.mm.domain.MedallionAssignment;
import com.carnival.mm.domain.MedallionTE2;
import com.carnival.mm.exception.MedallionCannotUpdateException;
import com.carnival.mm.exception.MedallionNotFoundException;
import com.carnival.mm.service.MedallionAssignmentTaskService;

import com.carnival.mm.service.MedallionService;
import com.carnival.mm.service.MedallionTE2Service;
import com.google.gson.Gson;
import org.apache.http.HttpRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;


import javax.validation.Valid;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Date;
import org.json.JSONObject;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;

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

        Medallion medallion = null;
        Date date = null;

        medallion.setId(medallionTE2.getId());
        medallion.setHardwareId(medallionTE2.getHardwareId());
        medallion.setUuId(medallionTE2.getUuid());
        medallion.setBleId(medallionTE2.getBleId());
        medallion.setMajorId(medallionTE2.getMajorId());
        medallion.setMinorId(medallionTE2.getMinorId());
        medallion.setUuId(medallionTE2.getUuid());
        medallion.setNfcId(medallionTE2.getNfcId());
        medallion.setStatus(medallionTE2.getStatus());
        medallion.setReservationId(medallionTE2.getReservationId());
        medallion.set__type(medallionTE2.get_eventType());
       // medallion.set_operation("create");
        medallion.set__version(medallionTE2.get_version());

        String dateStr = medallionTE2.get_timestamp();
        SimpleDateFormat ft = new SimpleDateFormat ("yyyy.MM.dd hh:mmm:ss");

        try{
            date = ft.parse(dateStr);
            medallion.setUpdated(date);
        }
        catch (Exception ex){
            System.out.println(ex);
        }

        medallionService.updateMedallion(medallion);
    }


    /**
     * Endpoint for assigning a medallion to an individual
     * @param medallionAssignment
     * @return
     */
    @RequestMapping(value="/v1/medallion-assignment", method=RequestMethod.POST)
    public Medallion assignMedallion(@Valid @RequestBody MedallionAssignment medallionAssignment){

        Medallion medallion = medallionAssignmentTaskService.assignMedallionToIndividual(medallionAssignment);
        MedallionTE2 medallionTE2 = getMedallionTE2(medallion);

        //New Code Added for invoking the URL

        String restURL = "https://dev-trident.te2.biz/rest/v1/event-subscriptions/event";

        Gson gson = new Gson();
        String jsonInString = gson.toJson(medallionTE2);

        JSONObject event = new JSONObject(jsonInString);

        HttpPost httpPost = createConnectivity(restURL);

        executeReq(jsonInString, httpPost);

        return medallion;
    }

    private HttpPost createConnectivity(String restUrl)
    {
        HttpPost post = new HttpPost(restUrl);
        post.setHeader("Content-Type", "application/json");
        post.setHeader("Accept", "application/json");
        post.setHeader("X-Stream" , "true");
        return post;
    }

    private void executeReq(String jsonData, HttpPost httpPost)
    {
        try{
            executeHttpRequest(jsonData, httpPost);
        }
        catch (IOException e){
            System.out.println("ioException occured while sending http request : "+e);
        }
        catch(Exception e){
            System.out.println("exception occured while sending http request : "+e);
        }
        finally{
            httpPost.releaseConnection();
        }
    }

    private void executeHttpRequest(String jsonData,  HttpPost httpPost)  throws IOException
    {
        HttpResponse response=null;
        String line = "";
        StringBuffer result = new StringBuffer();
        httpPost.setEntity(new StringEntity(jsonData));
        HttpClient client = HttpClientBuilder.create().build();
        response = client.execute(httpPost);
        BufferedReader reader = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
        while ((line = reader.readLine()) != null){
            result.append(line);
        }
        System.out.println(result.toString());
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
            throw new MedallionNotFoundException(medallion.getId());
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

