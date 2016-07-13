package com.carnival.mm.controller;

import com.carnival.mm.Application;
import com.carnival.mm.domain.Medallion;
import com.carnival.mm.repository.MedallionRepository;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.mock.http.MockHttpOutputMessage;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.Arrays;

import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Created by david.c.hoak on 6/23/2016.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
public class MedallionControllerTest {

    private MediaType contentType = new MediaType(MediaType.APPLICATION_JSON.getType(),
            MediaType.APPLICATION_JSON.getSubtype(),
            Charset.forName("utf8"));

    private MockMvc mockMvc;
    private Medallion medallion;
    private HttpMessageConverter mappingJackson2HttpMessageConverter;

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Autowired
    private MedallionRepository medallionRepository;

    @Autowired
    void setConverters(HttpMessageConverter<?>[] converters) {

        this.mappingJackson2HttpMessageConverter = Arrays.asList(converters).stream().filter(
                hmc -> hmc instanceof MappingJackson2HttpMessageConverter).findAny().get();

        Assert.assertNotNull("the JSON message converter must not be null",
                this.mappingJackson2HttpMessageConverter);
    }

    @Before
    public void setup() throws Exception {

        this.mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
        this.medallion = new Medallion();
        this.medallion.setId("123");
        this.medallion.setHardwareId("TEST123");
        this.medallion.setFirstName("FirstName");
        this.medallion.setLastName("LastName");
        medallionRepository.save(this.medallion);
    }

    @After
    public void tearDown() throws Exception {
        medallionRepository.delete(this.medallion);
    }

    @Test
    public void testCreateMedallion() throws Exception {

        String medallionJson = json(this.medallion);
        mockMvc.perform(post("/medallionservice/v1/medallion")
                .contentType(contentType)
                .content(medallionJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is("123")));
    }

    @Test
    public void testGetMedallionsByLastName() throws Exception {

        mockMvc.perform(get("/medallionservice/v1/medallions?lastName=LastName")).andExpect(status().isOk()).andExpect(jsonPath("$[0].firstName", is("FirstName")));

    }

    @Test
    public void testGetMedallionsByFirstAndLastName() throws Exception {
        mockMvc.perform(get("/medallionservice/v1/medallions?firstName=FirstName&lastName=LastName")).andExpect(status().isOk());
    }

    @Test
    public void testGetMedallionByHardwareId() throws Exception {

        mockMvc.perform(get("/medallionservice/v1/medallion/TEST123"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.hardwareId", is("TEST123")));
    }

    @Test
    public void testUpdateMedallion() throws Exception {

        this.medallion.setFirstName("ChangedFirstName");
        String medallionJson = json(this.medallion);
        mockMvc.perform(post("/medallionservice/v1/medallion")
                .contentType(contentType)
                .content(medallionJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName", is("ChangedFirstName")));
    }



    protected String json(Object o) throws IOException {
        MockHttpOutputMessage mockHttpOutputMessage = new MockHttpOutputMessage();
        this.mappingJackson2HttpMessageConverter.write(
                o, MediaType.APPLICATION_JSON, mockHttpOutputMessage);
        return mockHttpOutputMessage.getBodyAsString();
    }
}
