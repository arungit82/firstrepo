package com.carnival.mm;

import com.carnival.mm.controller.MedallionController;
import com.carnival.mm.domain.EventSubscriptionCallback;
import com.fasterxml.jackson.databind.ObjectMapper;
//import org.apache.commons.lang3.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.*;
import org.springframework.http.converter.FormHttpMessageConverter;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.util.Base64Utils;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.io.FileWriter;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by anbarasan.vincent on 10/4/2016.
 */
@Configuration
public class EventSubscriptionRegistration {
    private final Logger log = LoggerFactory.getLogger(this.getClass());
    @Bean
    public String RegisterSubscription() {
        //FIXME: Make it into the propoerties
        String ngrok_base = "http://1f33d830.ngrok.io/";
        String CALLBACK_URL = ngrok_base + "medallionservice/v1/medallion-callback";
       // String CALLBACK_URL = ngrok_base + "medallionservice/v1/guestOrders";
        String MEDALLION_ASSIGNMENT = "MedallionAssignment";
        //xiConnect QA Env
        String EVENT_POST_URL = "https://qa-trident.te2.biz/rest/v1/event-subscriptions/event";

        ResponseEntity<String> result = null;
        String accessToken = tokenPOSTcall();
        String medallionAssignEventPublishAsString = "";

        EventSubscriptionCallback eventSubscriptionCallback = new EventSubscriptionCallback();
        eventSubscriptionCallback.setCallbackURL(CALLBACK_URL);
        eventSubscriptionCallback.set_eventType(MEDALLION_ASSIGNMENT);

        //Headers
        HttpHeaders headers1 = new HttpHeaders();
        headers1.setContentType(MediaType.APPLICATION_JSON);
        if (accessToken != null)
            headers1.set("Authorization", "Bearer " + accessToken);

        //Body as JSON
        ObjectMapper mapper = new ObjectMapper();
        try {
            medallionAssignEventPublishAsString = mapper.writeValueAsString(eventSubscriptionCallback);
        } catch (Exception e) {
            e.printStackTrace();
        }

        HttpEntity<String> entityBody = new HttpEntity<String>(medallionAssignEventPublishAsString, headers1);

        List<HttpMessageConverter<?>> messageConverters = new ArrayList<HttpMessageConverter<?>>();

        RestTemplate restTemplate = new RestTemplate();

        messageConverters.add(new FormHttpMessageConverter());
        messageConverters.add(new StringHttpMessageConverter());
        restTemplate.setMessageConverters(messageConverters);
        try {
            result = restTemplate.exchange(EVENT_POST_URL, HttpMethod.POST, entityBody, String.class);
            System.out.println(result);
        } catch (Exception e) {
            e.printStackTrace();
          //  log.error(ExceptionUtils.getStackTrace(e));
        }

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

        } catch (Exception e) {
            e.printStackTrace();
        }
        return medallionAssignEventPublishAsString;
    }


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
}