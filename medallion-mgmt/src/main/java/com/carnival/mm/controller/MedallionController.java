package com.carnival.mm.controller;

import com.carnival.mm.domain.Medallion;
import com.carnival.mm.domain.MedallionAssignment;
import com.carnival.mm.domain.MedallionTE2;
import com.carnival.mm.exception.MedallionCannotUpdateException;
import com.carnival.mm.exception.MedallionNotFoundException;
import com.carnival.mm.service.MedallionAssignmentTaskService;
import com.carnival.mm.service.MedallionService;
import com.carnival.mm.service.MedallionTE2Service;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.http.converter.FormHttpMessageConverter;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.util.Base64Utils;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import javax.validation.Valid;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by david.c.hoak on 6/22/2016.
 */
@RestController
@RequestMapping("/medallionservice")
public class MedallionController {

    public static final Logger log = LoggerFactory.getLogger(MedallionController.class);
    @Autowired
    MedallionService medallionService;
    @Autowired
    MedallionAssignmentTaskService medallionAssignmentTaskService;
    @Autowired
    MedallionTE2Service medallionTE2Service;

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
     * Endpoint for receiving callback from xiConnect
     *
     * @param medallionTE2
     * @return
     */
    @RequestMapping(value = "/v1/medallion-callback", method = RequestMethod.POST)
    public Medallion updateMedallionAssignment(@Valid @RequestBody MedallionTE2 medallionTE2) {
        // To do: Call back assignment

        Medallion medallion = getMedallion(medallionTE2.getHardwareId());
        Date date = new Date();

/*        medallion.setGuestId(medallionTE2.getId());
        medallion.setHardwareId(medallionTE2.getHardwareId());
        medallion.setGuestId(medallionTE2.getUiid());
        medallion.setBleId(medallionTE2.getBleId());
        medallion.setMajorId(medallionTE2.getMajor());
        medallion.setMinorId(medallionTE2.getMinor());
        medallion.setUuId(medallionTE2.getUuid());
        medallion.setNfcId(medallionTE2.getNfcId());
        medallion.setStatus(medallionTE2.getStatus());
        medallion.setReservationId(medallionTE2.getReservationId());
        medallion.set__type(medallionTE2.get_eventType());
       // medallion.set_operation("create");
        medallion.set__version(medallionTE2.get_version());*/
        medallion.setStatus("ASSIGNED");

        String dateStr = medallionTE2.get_timestamp();
        SimpleDateFormat ft = new SimpleDateFormat("yyyy-MM-dd'T'hh:mm:ss'Z'");

        try {
            date = ft.parse(dateStr);
            medallion.setUpdated(date);
        } catch (Exception ex) {
            System.out.println(ex);
        }

        medallionService.updateMedallion(medallion);

        return medallion;
    }

    /*Function to retrieve OAuth2 token from ForgeRock
  Added by Anbu Vincent for Sprint 19
  Date: Sept 6, 2016*/

    public String tokenPOSTcall() {

        //Move the following to properties at a later time
        String accessToken = "";
        String GRANT_TYPE = "client_credentials";
        String SCOPE = "cn ocean";
        String USER_CREDENTIALS = "medallionManagement:C0llabor8!";
        String TOKEN_URL = "https://qa-trident.te2.biz/openam/oauth2/access_token";

        MultiValueMap<String, String> map = new LinkedMultiValueMap<String, String>();
        map.add("grant_type", GRANT_TYPE);
        //map.add("client_id", "medallionManagement");
        //map.add("client_secret", "C0llabor8!");
        map.add("scope", SCOPE);

        String plainCreds = USER_CREDENTIALS;
        byte[] plainCredsBytes = plainCreds.getBytes();
        byte[] base64CredsBytes = Base64Utils.encode(plainCredsBytes);
        String base64Creds = new String(base64CredsBytes);

        HttpHeaders headers1 = new HttpHeaders();
        headers1.add("Accept", MediaType.ALL_VALUE);
        headers1.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        headers1.add("Authorization", "Basic " + base64Creds);

        HttpEntity<MultiValueMap<String, String>> entity1 = new HttpEntity<MultiValueMap<String, String>>(map, headers1);

        List<HttpMessageConverter<?>> messageConverters = new ArrayList<HttpMessageConverter<?>>();

        RestTemplate restTemplate = new RestTemplate();

        messageConverters.add(new FormHttpMessageConverter());
        messageConverters.add(new StringHttpMessageConverter());
        restTemplate.setMessageConverters(messageConverters);

        //ResponseEntity<String> result = restTemplate.exchange("https://qa-trident.te2.biz/openam/oauth2/access_token",HttpMethod.POST,entity1,  String.class );
        ResponseEntity<String> result = restTemplate.exchange(TOKEN_URL, HttpMethod.POST, entity1, String.class);
        System.out.println(result);

        String[] parsedString = result.toString().split("\"");

        accessToken = parsedString[3];

        return accessToken;
    }

    /**
     * Endpoint for assigning a medallion to an individual
     *
     * @param medallionAssignment
     * @return
     */
    @RequestMapping(value = "/v1/medallion-assignment", method = RequestMethod.POST)
    public Medallion assignMedallion(@Valid @RequestBody MedallionAssignment medallionAssignment) {

        String accessToken = null;
        String medallionTE2AsString = "";
        String EVENT_POST_URL = "https://demo-trident.te2.biz/rest/v1/event-subscriptions/event";

        Medallion medallion = medallionAssignmentTaskService.assignMedallionToIndividual(medallionAssignment);
        MedallionTE2 medallionTE2 = getMedallionTE2(medallion);
        //Not needed to receive token for Demo environment
        //accessToken = tokenPOSTcall();

        //Headers
        HttpHeaders headers1 = new HttpHeaders();
        headers1.setContentType(MediaType.APPLICATION_JSON);
        if (accessToken != null)
            headers1.set("Authorization", "Bearer " + accessToken);

        //Body as JSON
        ObjectMapper mapper = new ObjectMapper();
        try {
            medallionTE2AsString = mapper.writeValueAsString(medallionTE2);
        } catch (Exception e) {
            e.printStackTrace();
        }

        HttpEntity<String> entityBody = new HttpEntity<String>(medallionTE2AsString, headers1);

        List<HttpMessageConverter<?>> messageConverters = new ArrayList<HttpMessageConverter<?>>();

        RestTemplate restTemplate = new RestTemplate();

        messageConverters.add(new FormHttpMessageConverter());
        messageConverters.add(new StringHttpMessageConverter());
        restTemplate.setMessageConverters(messageConverters);

        ResponseEntity<String> result = restTemplate.exchange(EVENT_POST_URL, HttpMethod.POST, entityBody, String.class);
        System.out.println(result);


        return medallion;

    }

    /**
     * Endpoint for getting medallion using Guest ID
     *
     * @param guestId
     * @return
     */
    @RequestMapping(value = "/v1/medallion-GUID/{guestId}", method = RequestMethod.GET)
    public List<Medallion> getMedallionGuestID(@PathVariable String guestId) {
        List<Medallion> medallions = medallionService.findMedallionByGuestID(guestId);
        if (medallions == null) {
            throw new MedallionNotFoundException(guestId);
        }
        return medallions;

    }

    /*Function to build datastructure for the Event publication
Added by Anbu Vincent for Sprint 19
Date: Sept 6, 2016*/

    private MedallionTE2 getMedallionTE2(Medallion medallion) {

        MedallionTE2 medallionTE2 = new MedallionTE2();
        // Medallion medallion = medallionService.findMedallionByHardwareId(hardwareId);

        if (medallion == null) {
            throw new MedallionNotFoundException(medallion.getId());
        } else {
            medallionTE2.setId(medallion.getId());
            medallionTE2.setHardwareId(medallion.getHardwareId());
            medallionTE2.setBleId(medallion.getBleId());
            medallionTE2.setMajor(medallion.getMajorId());
            medallionTE2.setMinor(medallion.getMinorId());
            medallionTE2.setUuid(medallion.getUuId());
            medallionTE2.setUiid(medallion.getGuestId());
            medallionTE2.setNfcId(medallion.getNfcId());
            medallionTE2.setStatus(medallion.getStatus());
            medallionTE2.setReservationId(medallion.getReservationId());
            medallionTE2.set_eventType("MedallionAssignment");
            medallionTE2.set_operation("create");
            medallionTE2.set_version("1.0");
            Date dNow = new Date();
            //SimpleDateFormat ft = new SimpleDateFormat("yyyy.MM.dd hh:mm:ss");
            SimpleDateFormat ft = new SimpleDateFormat("yyyy-MM-dd'T'hh:mm:ss'Z'");
            medallionTE2.set_timestamp(ft.format(dNow));
            //medallionTE2.setCallbackURL("http://fe71b312.ngrok.io/medallionservice/v1/medallion-callback");
        }
        return medallionTE2;
    }

    /**
     * Endpoint for getting the count of unassigned medallions.  This is a throw away method only to be used for
     * purposes of the release 9 experience slice demo.
     *
     * @return
     */
    @RequestMapping(value = "/v1/medallion-count", method = RequestMethod.GET)
    public int medallionUnassignedCount() {
        return medallionService.getAvailableMedallionCount();
    }


}

