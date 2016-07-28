package com.carnival.mm.service.impl;

import com.carnival.mm.domain.Medallion;
import com.carnival.mm.repository.MedallionRepository;
import com.carnival.mm.service.MedallionService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;

/**
 * Created by david.c.hoak on 6/27/2016.
 */
@RunWith(MockitoJUnitRunner.class)
public class MedallionServiceTest {

    @InjectMocks
    MedallionService medallionService;

    @Mock
    MedallionRepository medallionRepository;

    List<Medallion> allMedallions;

    @Before
    public void setup() throws Exception {

        MockitoAnnotations.initMocks(this);
        this.allMedallions = new ArrayList<Medallion>();
        for(int i = 0; i <5; i++){
            Medallion medallion = new Medallion();
            medallion.setId("0000" + i+1);
            medallion.setHardwareId("TEST00" + i+1);
            medallion.setFirstName("FirstName00" + i+1);
            medallion.setLastName("LastName00" + i+1);
            this.allMedallions.add(medallion);
        }
    }

    @Test
    public void testServiceConfiguration() {
        assertEquals("class com.carnival.mm.service.MedallionService", this.medallionService.getClass().toString());
    }

    @Test
    public void testGetAllMedallion(){
        when(medallionRepository.retrieveAll()).thenReturn(this.allMedallions);
        assertEquals(this.allMedallions.size(), medallionService.getAllMedallions().size());
    }

    @Test
    public void testCreateMedallion(){
        Medallion medallion = allMedallions.get(0);
        when(medallionRepository.save(medallion)).thenReturn(medallion);
        assertEquals(medallion, medallionService.createMedallion(medallion));
    }

    @Test
    public void testFindMedallionByName(){
        when(medallionRepository.findByName(any())).thenReturn(this.allMedallions);
        List<Medallion> medallions = medallionService.searchMedallionsByName("FirstName", "LastName");
        assertEquals(this.allMedallions, medallions);
    }



}
