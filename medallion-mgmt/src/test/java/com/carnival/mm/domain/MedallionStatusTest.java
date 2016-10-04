package com.carnival.mm.domain;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

/**
 * Created by david on 8/2/16.
 */
@RunWith(MockitoJUnitRunner.class)
public class MedallionStatusTest {

    @Test
    public void testUnassignedStatus(){
        //ACTIVE
        System.out.println(MedallionStatus.UNASSIGNED);
    }
}
