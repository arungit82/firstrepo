package com.carnival.mm.service;

import com.carnival.mm.domain.Medallion;
import com.carnival.mm.domain.MedallionAssignment;
import com.carnival.mm.domain.MedallionStatus;
import com.carnival.mm.exception.MedallionNotAssignableException;
import com.carnival.mm.exception.MedallionNotFoundException;
import com.carnival.mm.repository.MedallionRepository;
import com.couchbase.client.protocol.views.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.integration.support.MessageBuilder;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

/**
 * Created by david on 7/26/16.
 */
@Service
public class MedallionAssignmentTaskService {

    @Autowired
    MedallionService medallionService;

//    private MedallionChannels channels;

//    @Autowired
//    public MedallionAssignmentTaskService(MedallionChannels channels) {
//        this.channels = channels;
//    }

    /**
     * This is a proof of concept method for publishing a business event on kafka
     * @param name
     */
//    public void sayHello(String name) {
//        channels.assignments().send(MessageBuilder.withPayload("Hello " + name).build());
//    }


    /**
     * Business logic for assigning the medallion to an individual
     * @param assignment
     * @return
     */
    public Medallion assignMedallionToIndividual(MedallionAssignment assignment){

        Medallion medallion = medallionService.findMedallionByHardwareId(assignment.getHardwareId());

        if(medallion != null){
            //Check medallion status
            if(!medallion.getStatus().equals(MedallionStatus.UNASSIGNED.toString()))
            {
                //throw exception
                throw new MedallionNotAssignableException(assignment.getHardwareId());
            }
        }
        else
        {
            throw new MedallionNotFoundException(assignment.getHardwareId());
        }

        //TODO Need to call IIH to make sure the UIID is not already assigned to a medallion

        medallion.setGuestId(assignment.getUiid());
        medallion.setFirstName(assignment.getFirstName());
        medallion.setLastName(assignment.getLastName());
        medallion.setStatus(MedallionStatus.ASSIGNED.toString());

        Medallion updatedMedallion = medallionService.updateMedallion(medallion);


        //TODO Need to publish Medallion Assignment to xiConnect

        return updatedMedallion;
    }



}
