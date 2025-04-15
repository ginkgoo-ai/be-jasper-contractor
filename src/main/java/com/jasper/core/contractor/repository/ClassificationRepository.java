package com.jasper.core.contractor.repository;

import com.jasper.core.contractor.domain.classification.Classification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;


@Repository
public interface ClassificationRepository extends JpaRepository<Classification, String>, JpaSpecificationExecutor<Classification> {
}
