package com.jasper.core.contractor.domain.contractor;

import com.jasper.core.contractor.domain.BaseLogicalDeleteEntity;
import jakarta.persistence.*;
import lombok.*;

import java.sql.Timestamp;
import java.util.Date;

@Getter
@Setter
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "contractor")
public class Contractor extends BaseLogicalDeleteEntity {

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

    private String classification;

    private Date issueDate;
    private Date expirationDate;
    private Date lastUpdated;

    private Double geoLat;
    private Double geoLng;
    private Timestamp createdAt;
    private Timestamp updatedAt;
    private String createdBy;
    private String updatedBy;

}