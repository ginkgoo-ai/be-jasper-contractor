package com.jasper.core.contractor.repository;

import com.jasper.core.contractor.domain.contractor.Contractor;
import com.jasper.core.contractor.jpa.support.AbstractRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface ContractorRepository extends AbstractRepository<Contractor, String> {


}
