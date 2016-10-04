package com.carnival.mm.controller;

import com.carnival.mm.domain.*;
import com.carnival.mm.exception.MedallionCannotUpdateException;
import com.carnival.mm.exception.MedallionNotFoundException;
import com.carnival.mm.service.MedallionAssignmentTaskService;
import com.carnival.mm.service.MedallionService;
import com.carnival.mm.service.MedallionAssignEventPubService;
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
import java.io.FileWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
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
    MedallionAssignEventPubService medallionAssignEventPubService;

    /*
    @Value("${log_file}")
    String filePath;
*/
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
        //medallion.set__version((Float.parseFloat(medallion.get__version())+1)+"");
        //Don't update the record but create a new version
        //return medallionService.createMedallion(medallion);
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
     * @param medallionAssignEventPublish
     * @return
     */
    @RequestMapping(value = "/v1/medallion-callback", method = RequestMethod.POST)
    public Medallion updateMedallionAssignment(@Valid @RequestBody MedallionAssignEventPublish medallionAssignEventPublish) {
        // To do: Call back assignment

        Medallion medallion = getMedallion(medallionAssignEventPublish.getHardwareId());

        System.out.println("Inside Callback");
        ObjectMapper mapper = new ObjectMapper();
        String medallionAsString = "";
        try {
            medallionAsString = mapper.writeValueAsString(medallion);
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println(medallionAsString);


        Date date = new Date();

        medallion.setStatus("ASSIGNED");

        String dateStr = medallionAssignEventPublish.get_timestamp();
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

        //FIXME: Move the following to properties at a later time
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
        String medallionAssignEventPublishAsString = "";
        //xiConnect Demo Env
        //String EVENT_POST_URL = "https://demo-trident.te2.biz/rest/v1/event-subscriptions/event";
        //xiConnect QA Env
        String EVENT_POST_URL = "https://qa-trident.te2.biz/rest/v1/event-subscriptions/event";

        Medallion medallion = medallionAssignmentTaskService.assignMedallionToIndividual(medallionAssignment);
        MedallionAssignEventPublish medallionAssignEventPublish = getMedallionAssignEventPublish(medallion);
        //Not needed to receive token for Demo environment
        accessToken = tokenPOSTcall();

        //Headers
        HttpHeaders headers1 = new HttpHeaders();
        headers1.setContentType(MediaType.APPLICATION_JSON);
        if (accessToken != null)
            headers1.set("Authorization", "Bearer " + accessToken);

        //Body as JSON
        ObjectMapper mapper = new ObjectMapper();
        try {
            medallionAssignEventPublishAsString = mapper.writeValueAsString(medallionAssignEventPublish);
        } catch (Exception e) {
            e.printStackTrace();
        }

        HttpEntity<String> entityBody = new HttpEntity<String>(medallionAssignEventPublishAsString, headers1);

        List<HttpMessageConverter<?>> messageConverters = new ArrayList<HttpMessageConverter<?>>();

        RestTemplate restTemplate = new RestTemplate();

        messageConverters.add(new FormHttpMessageConverter());
        messageConverters.add(new StringHttpMessageConverter());
        restTemplate.setMessageConverters(messageConverters);

        ResponseEntity<String> result = restTemplate.exchange(EVENT_POST_URL, HttpMethod.POST, entityBody, String.class);
        System.out.println(result);

        try {
            String filePath = "request_response_data.txt";
            FileWriter writer = new FileWriter(filePath, true);
            writer.write(medallionAssignEventPublishAsString);
            writer.write("\n");
            writer.write("=========================");
            writer.write("\n");
            writer.write("\n");
            writer.write(result.toString());
            writer.write("\n");
            writer.write("\n");
            writer.write("\n");
            writer.write("\n");
            writer.close();

        }catch (Exception e){
            e.printStackTrace();
        }

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

    private MedallionAssignEventPublish getMedallionAssignEventPublish(Medallion medallion) {

        MedallionAssignEventPublish medallionAssignEventPublish = new MedallionAssignEventPublish();
        // Medallion medallion = medallionService.findMedallionByHardwareId(hardwareId);

        if (medallion == null) {
            throw new MedallionNotFoundException(medallion.getId());
        } else {
            medallionAssignEventPublish.setId(medallion.getId());
            medallionAssignEventPublish.setHardwareId(medallion.getHardwareId());
            medallionAssignEventPublish.setBleId(medallion.getBleId());
            medallionAssignEventPublish.setMajor(medallion.getMajorId());
            medallionAssignEventPublish.setMinor(medallion.getMinorId());
            medallionAssignEventPublish.setUuid(medallion.getUuId());
            medallionAssignEventPublish.setUiid(medallion.getGuestId());
            medallionAssignEventPublish.setNfcId(medallion.getNfcId());
            medallionAssignEventPublish.setStatus(medallion.getStatus());
            medallionAssignEventPublish.setReservationId(medallion.getReservationId());
            medallionAssignEventPublish.set_eventType("MedallionAssignment");
            medallionAssignEventPublish.set_operation("create");
            medallionAssignEventPublish.set_version("1.0");
            Date dNow = new Date();
            //SimpleDateFormat ft = new SimpleDateFormat("yyyy.MM.dd hh:mm:ss");
            SimpleDateFormat ft = new SimpleDateFormat("yyyy-MM-dd'T'hh:mm:ss'Z'");
            medallionAssignEventPublish.set_timestamp(ft.format(dNow));
        }
        return medallionAssignEventPublish;
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

    /**
     * Endpoint for getting medallion History using Hardware ID
     *
     * @param hardwareId
     * @return
     */
    @RequestMapping(value = "/v1/medallionHistory/{hardwareId}", method = RequestMethod.GET)
    public List<Medallion> getMedallionHistoryByHardwareID(@PathVariable String hardwareId) {
        List<Medallion> medallionsHistory = medallionService.findMedallionHistoryByHardwareId(hardwareId);
        if (medallionsHistory == null) {
            throw new MedallionNotFoundException(hardwareId);
        }
        return medallionsHistory;

    }

    /**
     * Endpoint for getting the orders from xiconnect and process the orders
     *
     * @param orderDetails
     * @return
     */

    @RequestMapping(value = "/v1/guestOrders",method = RequestMethod.POST)
    public ResponseEntity<List<GuestOrder>> createGuestOrder(@RequestBody String orderDetails){
        log.info("Inside MedallionController-->createGuestOrder()");
        List<GuestOrder> guestOrderList = parseAndCreateOrder(orderDetails);
        List<GuestOrder> savedGuestOrders = new ArrayList<>();
        //Loop Orders and save to datastore
        for(GuestOrder order:guestOrderList){
            savedGuestOrders.add(medallionService.createGuestOrder(order));
        }
        return new ResponseEntity<List<GuestOrder>>(savedGuestOrders,HttpStatus.CREATED);
    }
    private List<GuestOrder> parseAndCreateOrder(String orderDetails){
        List<GuestOrder> orderList = new ArrayList<>();
        try {
            String firstName,lastName,addressId,organization,street1,street2,city,region,country,zipPostalCode,phoneNumber;
            GuestOrder shippingOrder = null;
            ShippingAddress address= null;
            String productCode;
            JSONObject shippingAddress = null;
            JSONObject order = new JSONObject(orderDetails);
            JSONObject header = order.getJSONObject("header");
            String voyageId = header.getString("voyageId");
            //Fetching Shipment JSON data
            JSONArray shipmentInfo = header.getJSONArray("shipment");

            //Looping Shipping Data Array and preparing the order List
            for(int i=0;i<shipmentInfo.length();i++){
                JSONObject shippingObj = (JSONObject)shipmentInfo.get(i);

               if(shippingObj !=null){
                   shippingOrder = new GuestOrder();
                   JSONArray lineItemArray = shippingObj.getJSONArray("lineItem");
                   //Loop LineItem Array and fetch data only if product code is Medallion
                   for(int j=0;j<lineItemArray.length();j++){
                       JSONObject lineItemObj = (JSONObject)lineItemArray.get(j);
                       if(lineItemObj != null){
                           productCode = lineItemObj.getString("productCode");
                           //Set guest id only if the product code is Medallion
                           if(productCode != null && productCode.equals("Medallion")){
                               shippingOrder.setGuestId(lineItemObj.getString("guestId"));
                           }
                       }
                   }
                   //Set Shipping address only if guest Id is present
                  if(shippingOrder != null && !shippingOrder.getGuestId().equals("")) {
                      shippingAddress = shippingObj.getJSONObject("shippingAddress");
                      //Set Shipping Address
                      address = setShippingAddress(shippingAddress,address,shippingOrder);
                      shippingOrder.setShippingAddress(address);
                      shippingOrder.setVoyageId(voyageId);
                    // Have to get voyage date & reservation id by calling another API
                      //Temporarily hardcoding voyage date & reservation id--Remove this later
                      shippingOrder.setVoyageDate(String.valueOf(new Date()));
                      shippingOrder.setReservationId("12345");

                      orderList.add(shippingOrder);
                  }
               }
            }

            System.out.println("shippingOrder-->"+orderList);
        } catch (JSONException e) {
            log.error("An error occured while parsing JSON "+e);
            e.printStackTrace();
        }
        return orderList;
    }

    private ShippingAddress setShippingAddress(JSONObject shippingAddress,ShippingAddress address,GuestOrder shippingOrder){
        String firstName,lastName,addressId,organization,street1,street2,city,region,country,zipPostalCode,phoneNumber;
        firstName = shippingAddress.getString("firstName");
        lastName = shippingAddress.getString("lastName");
        addressId = shippingAddress.getString("addressId");
        //   organization = (shippingAddress.getString("organization") != null)?(shippingAddress.getString("organization")):"";
        organization = (shippingAddress.get("organization") instanceof String) ? (shippingAddress.getString("organization")) : "";
        street1 = shippingAddress.getString("street1");
        street2 = shippingAddress.getString("street2");
        city = shippingAddress.getString("city");
        region = shippingAddress.getString("region");
        country = shippingAddress.getString("country");
        zipPostalCode = shippingAddress.getString("zipPostalCode");
        phoneNumber = shippingAddress.getString("phoneNumber");

        address = new ShippingAddress();
        address.setFirstName(firstName);
        address.setLastName(lastName);
        address.setAddressId(addressId);
        address.setOrganization(organization);
        address.setStreet1(street1);
        address.setStreet2(street2);
        address.setCity(city);
        address.setRegion(region);
        address.setCountry(country);
        address.setZipPostalCode(zipPostalCode);
        address.setPhoneNumber(phoneNumber);

        shippingOrder.setFirstName(firstName);
        shippingOrder.setLastName(lastName);
        return address;
    }
}

