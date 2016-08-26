package com.carnival.mm.service;

/**
 * Created by ps.r.balamurugan on 26-08-2016.
 */

import com.carnival.mm.domain.Medallion;
import com.carnival.mm.domain.MedallionTE2;
import com.carnival.mm.repository.MedallionRepository;
import com.couchbase.client.protocol.views.Query;
import com.couchbase.client.protocol.views.Stale;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("medallionTE2Service")
public class MedallionTE2Service {

    @Autowired
    private MedallionRepository medallionRepository;

    private static Logger log = LoggerFactory.getLogger(MedallionTE2Service.class);

    /**
     * Find Medallion from the data store by hardwareId
     *
     * @param hardwareId
     * @return
     */
    public MedallionTE2 findMedallionByHardwareId(String hardwareId) {
        Query query = new Query();
        query.setKey(hardwareId);
        query.setStale(Stale.FALSE);

        return medallionRepository.findByHardwareIdTE2(query);
    }
}
