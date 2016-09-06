package com.carnival.mm.repository;

import com.carnival.mm.domain.Medallion;
import com.carnival.mm.domain.MedallionOphir;
import com.carnival.mm.domain.MedallionTE2;
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
     * Repository method for corresponding view byHardwareId
     * @param hardwareId
     * @return
     */
    MedallionTE2 findByHardwareIdTE2(Query hardwareId);

    /**
     * Repository method for corresponding view byUIID
     * @param uuId
     * @return
     */
    MedallionOphir findByUIID(Query uuId);

    /**
     * Repository method for corresponding view byGuestId
     * @param guestId
     * @return
     */
    List<Medallion> findByGuestId(Query guestId);

    /**
     * Repository method for corresponding view byLocatorId
     * @param reservationId
     * @return
     */
    List<Medallion> findByReservationId(Query reservationId);

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

    /**
     * Repository method for corresponding view byStatus
     * @return
     */
    List<Medallion> findByStatus(Query status);
}
