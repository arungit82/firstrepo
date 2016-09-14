package com.carnival.mm.service;

/**
 * Created by ps.r.balamurugan on 26-08-2016.
 */

import com.carnival.mm.domain.MedallionAssignEventPublish;
import com.carnival.mm.repository.MedallionRepository;
import com.couchbase.client.protocol.views.Query;
import com.couchbase.client.protocol.views.Stale;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("medallionAssignEventPubService")
public class MedallionAssignEventPubService {

    @Autowired
    private MedallionRepository medallionRepository;

    private static Logger log = LoggerFactory.getLogger(MedallionAssignEventPubService.class);

    /**
     * Find Medallion from the data store by hardwareId
     *
     * @param hardwareId
     * @return
     */
    public MedallionAssignEventPublish findMedallionByHardwareId(String hardwareId) {
        Query query = new Query();
        query.setKey(hardwareId);
        query.setStale(Stale.FALSE);

        return medallionRepository.findByHardwareIdTAssignEventPublish(query);
    }
}
