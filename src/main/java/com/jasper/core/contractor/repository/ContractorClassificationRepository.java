package com.jasper.core.contractor.repository;

import com.jasper.core.contractor.domain.contractor.ContractorClassification;
import com.jasper.core.contractor.jpa.support.AbstractRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface ContractorClassificationRepository extends AbstractRepository<ContractorClassification,String> {

    default long deleteByContractorId(String contractorId) {
        return delete(buildSpecification(it->it.when(ContractorClassification::getContractorId).eq(contractorId)));
    }
}
