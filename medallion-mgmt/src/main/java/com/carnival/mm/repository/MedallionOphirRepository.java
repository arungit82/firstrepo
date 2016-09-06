package com.carnival.mm.repository;

import com.carnival.mm.domain.MedallionOphir;
import com.couchbase.client.protocol.views.Query;
import org.springframework.data.repository.CrudRepository;

/**
 * Created by ps.r.balamurugan on 06-09-2016.
 */
public interface MedallionOphirRepository  extends CrudRepository<MedallionOphir, String> {
    /**
     * Repository method for corresponding view byUIID
     * @param uuId
     * @return
     */
    MedallionOphir findByUuId(Query uuId);
}
