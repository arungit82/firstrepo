package com.carnival.mm.controller;

import com.carnival.mm.Application;
import com.carnival.mm.domain.Medallion;
import com.carnival.mm.service.MedallionService;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;
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
    private List<Medallion> medallionList = new ArrayList<>();
    private HttpMessageConverter mappingJackson2HttpMessageConverter;

    @InjectMocks
    private MedallionController medallionController;

    @Mock
    private MedallionService medallionService;

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Autowired
    void setConverters(HttpMessageConverter<?>[] converters) {

        this.mappingJackson2HttpMessageConverter = Arrays.asList(converters).stream().filter(
                hmc -> hmc instanceof MappingJackson2HttpMessageConverter).findAny().get();

        Assert.assertNotNull("the JSON message converter must not be null",
                this.mappingJackson2HttpMessageConverter);
    }

    @Before
    public void setup() throws Exception {

        // Process mock annotations
        MockitoAnnotations.initMocks(this);
        // Setup Spring test in standalone mode
        this.mockMvc = MockMvcBuilders.standaloneSetup(medallionController).build();

        this.medallion = new Medallion();
        this.medallion.setId("123");
        this.medallion.setHardwareId("TEST123");
        this.medallion.setBleId("testBle");
        this.medallion.setMajorId("testMajor");
        this.medallion.setMinorId("testMinor");
        this.medallion.setNfcId("testNfc");
        this.medallion.setUuId("testUuid");
        this.medallion.setStatus("ASSIGNED");
        this.medallion.setFirstName("FirstName");
        this.medallion.setLastName("LastName");

        this.medallionList.add(this.medallion);
    }

    @Test
    public void testCreateMedallion() throws Exception {

        String medallionJson = json(this.medallion);
        when(medallionService.createMedallion(any(Medallion.class))).thenReturn(this.medallion);

        mockMvc.perform(post("/medallionservice/v1/medallion")
                .contentType(contentType)
                .content(medallionJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is("123")));
    }

    @Test
    public void testGetMedallionsByLastName() throws Exception {

        when(medallionService.searchMedallionsByName(any(), any())).thenReturn(this.medallionList);

        mockMvc.perform(get("/medallionservice/v1/medallions?lastName=LastName"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].firstName", is("FirstName")));

    }

    @Test
    public void testGetMedallionsByFirstAndLastName() throws Exception {

        when(medallionService.searchMedallionsByName(any(), any())).thenReturn(this.medallionList);

        mockMvc.perform(get("/medallionservice/v1/medallions?firstName=FirstName&lastName=LastName"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].firstName", is("FirstName")));
    }

    @Test
    public void testGetMedallionsByFirstAndLastNameIgnoreCase() throws Exception {

        when(medallionService.searchMedallionsByName(any(), any())).thenReturn(this.medallionList);

        mockMvc.perform(get("/medallionservice/v1/medallions?firstName=firstName&lastName=lastName"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].firstName", is("FirstName")));
    }

    @Test
    public void testGetMedallionByHardwareId() throws Exception {

        when(medallionService.findMedallionByHardwareId(any())).thenReturn(this.medallion);

        mockMvc.perform(get("/medallionservice/v1/medallion/TEST123"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.hardwareId", is("TEST123")));
    }

    @Test
    public void testUpdateMedallion() throws Exception {

        when(medallionService.updateMedallion(any())).thenReturn(this.medallion);

        this.medallion.setFirstName("ChangedFirstName");
        String medallionJson = json(this.medallion);
        mockMvc.perform(post("/medallionservice/v1/medallion/TEST123")
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
