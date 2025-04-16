package com.jasper.core.contractor.repository;

import com.jasper.core.contractor.domain.contractor.Contractor;
import com.jasper.core.contractor.domain.contractor.ContractorQueryResult;
import com.jasper.core.contractor.jpa.support.AbstractRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ContractorRepository extends AbstractRepository<Contractor, String> {


    default Optional<Contractor> findByLicenseNumber(String licenseNumber) {
        return findOne(it -> it.when(Contractor::getLicenseNumber).eq(licenseNumber));
    }

    @Query(
            value = "select * from (" +
                    "   select tmp.id,tmp.license_number,tmp.business_type,tmp.business_name,tmp.address,tmp.county," +
                    "       tmp.city,tmp.state,tmp.zip,tmp.phone_number,tmp.issue_date,tmp.expiration_date,tmp.last_updated," +
                    "       tmp.geo_lat,tmp.geo_lng,tmp.data_source,tmp.status,tmp.classification,tmp.classification_array," +
                    "       tmp.created_at,tmp.updated_at,tmp.created_by,tmp.updated_by, " +
                    "       round(earth_distance(ll_to_earth(tmp.geo_lat, tmp.geo_lng), ll_to_earth(:lat ,:lng ))::numeric,2) as distance "+
                    "   from (" +
                    "       select * " +
                    "       from contractor c " +
                    "       where (:city is null or c.city ilike concat('%',:city,'%')) " +
                    "           and (:state is null or c.state = :state) " +
                    "           and (:licenseNumber is null or c.license_number = :licenseNumber) " +
                    "           and (:classifications is null or jsonb_exists_any(c.classification_array::jsonb, :classifications) ) " +
                    "       ) tmp " +
                    "   ) filtered "+
                    "where (:radius is null or distance <= :radius) "+
                    "order by ?#{#pageable}\n",
            countQuery = "select count(1) " +
                    "from contractor " +
                    "where (:city is null or city ilike concat('%',:city,'%')) " +
                    "   and (:state is null or state = :state )  " +
                    "   and (:licenseNumber is null or license_number = :licenseNumber )  " +
                    "   and (:classifications is null or jsonb_exists_any(classification_array::jsonb, :classifications) ) " +
                    "   and (:radius is null or earth_distance(ll_to_earth(geo_lat, geo_lng), ll_to_earth(:lat ,:lng )) <= :radius ) ",
            nativeQuery = true
    )
    Page<ContractorQueryResult> pagination(@Param("lat") Double lat,
                                           @Param("lng") Double lng,
                                           @Param("radius") Double radius,
                                           @Param("city") String city,
                                           @Param("licenseNumber") String licenseNumber,
                                           @Param("state") String state,
                                           @Param("classifications")  String[] classifications,
                                           Pageable pageable);

}
