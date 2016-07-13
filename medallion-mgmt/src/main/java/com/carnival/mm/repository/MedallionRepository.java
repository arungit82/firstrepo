package com.carnival.mm.repository;

import com.carnival.mm.domain.Medallion;
import com.couchbase.client.protocol.views.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

/**
 * Created by david.c.hoak on 6/22/2016.
 */
public interface MedallionRepository extends CrudRepository<Medallion, String> {

    /**
     * Repository method for corresponding view byHardwareId
     * @param hardwareId
     * @return
     */
    Medallion findByHardwareId(Query hardwareId);

    /**
     * Repository method for corresponding view byLocatorId
     * @param locatorId
     * @return
     */
    List<Medallion> findByLocatorId(Query locatorId);

    /**
     * Repository method for corresponding view byName
     * @param name
     * @return
     */
    List<Medallion> findByName(Query name);

    /**
     * Repository method for corresponding view retrieveAll
     * @return
     */
    List<Medallion> retrieveAll();
}
