package com.jasper.core.contractor.dto.response;

import lombok.Data;

import java.util.Objects;

@Data
public class CslbContractor {
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

    private String issueDate;
    private String expirationDate;
    private String lastUpdated;
    private String status;

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        CslbContractor that = (CslbContractor) o;
        return Objects.equals(licenseNumber, that.licenseNumber) && Objects.equals(businessType, that.businessType) && Objects.equals(businessName, that.businessName) && Objects.equals(address, that.address) && Objects.equals(county, that.county) && Objects.equals(city, that.city) && Objects.equals(state, that.state) && Objects.equals(zip, that.zip) && Objects.equals(phoneNumber, that.phoneNumber) && Objects.equals(classification, that.classification) && Objects.equals(issueDate, that.issueDate) && Objects.equals(expirationDate, that.expirationDate) && Objects.equals(lastUpdated, that.lastUpdated) && Objects.equals(status, that.status);
    }

    @Override
    public int hashCode() {
        return Objects.hash(licenseNumber, businessType, businessName, address, county, city, state, zip, phoneNumber, classification, issueDate, expirationDate, lastUpdated, status);
    }

    @Override
    public String toString() {
        return "CslbContractor{" +
                "licenseNumber='" + licenseNumber + '\'' +
                ", businessType='" + businessType + '\'' +
                ", businessName='" + businessName + '\'' +
                ", address='" + address + '\'' +
                ", county='" + county + '\'' +
                ", city='" + city + '\'' +
                ", state='" + state + '\'' +
                ", zip='" + zip + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", classification='" + classification + '\'' +
                ", issueDate=" + issueDate +
                ", expirationDate=" + expirationDate +
                ", lastUpdated=" + lastUpdated +
                ", status='" + status + '\'' +
                '}';
    }
}
