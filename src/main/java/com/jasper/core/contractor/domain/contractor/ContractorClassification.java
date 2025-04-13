package com.jasper.core.contractor.domain.contractor;

import jakarta.persistence.*;
import lombok.*;

import java.util.Objects;

@Builder
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "contractor_classification")
public class ContractorClassification {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    private String contractorId;

    private String classificationId;

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        ContractorClassification that = (ContractorClassification) o;
        return Objects.equals(contractorId, that.contractorId) && Objects.equals(classificationId, that.classificationId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(contractorId, classificationId);
    }
}
