package com.carnival.mm.service;

import com.carnival.mm.domain.MedallionOphir;
import com.carnival.mm.repository.MedallionOphirRepository;
import com.carnival.mm.repository.MedallionRepository;
import com.couchbase.client.protocol.views.Query;
import com.couchbase.client.protocol.views.Stale;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by ps.r.balamurugan on 06-09-2016.
 */
@Service("medallionOphirService")
public class MedallionOphirService {

    @Autowired
    private MedallionOphirRepository medallionRepository;

    private static Logger log = LoggerFactory.getLogger(MedallionOphirService.class);

    /**
     * Find Medallion from the datastore by hardwareId
     *
     * @param uuId
     * @return
     */
    public MedallionOphir findMedallionByUuId(String uuId) {
        Query query = new Query();
        query.setKey(uuId);
        query.setStale(Stale.FALSE);

        return medallionRepository.findByUuId(query);
    }
}
