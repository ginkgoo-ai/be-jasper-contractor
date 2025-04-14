package com.jasper.core.contractor.domain.contractor;

import com.jasper.core.contractor.domain.BaseAuditableEntity;
import io.hypersistence.utils.hibernate.type.json.JsonType;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Type;

import java.util.List;


@Entity
@Builder
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "contractor")
public class Contractor extends BaseAuditableEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    private String licenseNumber;
    private String businessType;
    private String businessName;
    private String address;
    private String county;
    private String city;
    private String state;
    private String zip;
    private String phoneNumber;

    private String issueDate;
    private String expirationDate;
    private String lastUpdated;

    private Double geoLat;
    private Double geoLng;

    private String dataSource;
    private String status;

    private String classification;

    @Type(JsonType.class)
    private List<String> classificationArray;

}