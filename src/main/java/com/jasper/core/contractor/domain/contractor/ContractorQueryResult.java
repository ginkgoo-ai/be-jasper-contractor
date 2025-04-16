package com.jasper.core.contractor.domain.contractor;

import io.swagger.v3.oas.annotations.media.Schema;


import lombok.*;

import java.math.BigDecimal;
import java.sql.Timestamp;

@Data
@Builder
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ContractorQueryResult {

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

    private String classificationArray;

    private Timestamp createdAt;

    private Timestamp updatedAt;

    private String createdBy;

    private String updatedBy;

    private BigDecimal distance;
}
