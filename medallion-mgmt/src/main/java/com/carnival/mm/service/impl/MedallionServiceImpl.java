package com.carnival.mm.service.impl;

import com.carnival.mm.domain.Medallion;
import com.carnival.mm.repository.MedallionRepository;
import com.carnival.mm.service.MedallionService;
import com.couchbase.client.protocol.views.Query;
import com.google.common.collect.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Created by david.c.hoak on 6/22/2016.
 */
@Component("medallionService")
public class MedallionServiceImpl implements MedallionService {

    @Autowired
    MedallionRepository medallionRepository;

    @Override
    public List<Medallion> getAllMedallions() {

        return Lists.newArrayList(medallionRepository.retrieveAll());
    }

    @Override
    public Medallion getMedallionByHardwareId(String hardwareId) {
        Query query = new Query();
        query.setKey(hardwareId);
        return medallionRepository.findByHardwareId(query);
    }

    @Override
    public Medallion createMedallion(Medallion medallion) {
        return medallionRepository.save(medallion);
    }
}
