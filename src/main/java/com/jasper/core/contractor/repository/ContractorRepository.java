package com.jasper.core.contractor.repository;

import com.jasper.core.contractor.domain.contractor.Contractor;
import com.jasper.core.contractor.jpa.support.AbstractRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ContractorRepository extends AbstractRepository<Contractor, String> {


    default Optional<Contractor> findByLicenseNumber(String licenseNumber) {
        return findOne(it -> it.when(Contractor::getLicenseNumber).eq(licenseNumber));
    }

}
