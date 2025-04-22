package com.jasper.core.contractor.domain.contractor;

import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.sql.Timestamp;

@Data
@Builder
@Setter
@Getter
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

    public ContractorQueryResult() {}

    public ContractorQueryResult(String id, String licenseNumber, String businessType, String businessName, String address, String county, String city, String state, String zip, String phoneNumber, String issueDate, String expirationDate, String lastUpdated, Double geoLat, Double geoLng, String dataSource, String status, String classification, String classificationArray, Timestamp createdAt, Timestamp updatedAt, String createdBy, String updatedBy) {
        this.id = id;
        this.licenseNumber = licenseNumber;
        this.businessType = businessType;
        this.businessName = businessName;
        this.address = address;
        this.county = county;
        this.city = city;
        this.state = state;
        this.zip = zip;
        this.phoneNumber = phoneNumber;
        this.issueDate = issueDate;
        this.expirationDate = expirationDate;
        this.lastUpdated = lastUpdated;
        this.geoLat = geoLat;
        this.geoLng = geoLng;
        this.dataSource = dataSource;
        this.status = status;
        this.classification = classification;
        this.classificationArray = classificationArray;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.createdBy = createdBy;
        this.updatedBy = updatedBy;
    }

    public ContractorQueryResult(String id, String licenseNumber, String businessType, String businessName, String address, String county, String city, String state, String zip, String phoneNumber, String issueDate, String expirationDate, String lastUpdated, Double geoLat, Double geoLng, String dataSource, String status, String classification, String classificationArray, Timestamp createdAt, Timestamp updatedAt, String createdBy, String updatedBy, BigDecimal distance) {
        this.id = id;
        this.licenseNumber = licenseNumber;
        this.businessType = businessType;
        this.businessName = businessName;
        this.address = address;
        this.county = county;
        this.city = city;
        this.state = state;
        this.zip = zip;
        this.phoneNumber = phoneNumber;
        this.issueDate = issueDate;
        this.expirationDate = expirationDate;
        this.lastUpdated = lastUpdated;
        this.geoLat = geoLat;
        this.geoLng = geoLng;
        this.dataSource = dataSource;
        this.status = status;
        this.classification = classification;
        this.classificationArray = classificationArray;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.createdBy = createdBy;
        this.updatedBy = updatedBy;
        this.distance = distance;
    }
}
