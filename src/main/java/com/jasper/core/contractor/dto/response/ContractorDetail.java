package com.jasper.core.contractor.dto.response;

import com.jasper.core.contractor.aspect.annotation.ExcelColumn;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.List;

@Data
@Builder
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Details about a Contractor")
public class ContractorDetail {


    @Schema(description = "The Contractor ID")
    private String id;

    @ExcelColumn("License Number")
    @Schema(description = "The license number")
    private String licenseNumber;

    @ExcelColumn("Business Type")
    @Schema(description = "The business type")
    private String businessType;

    @ExcelColumn("Business Name")
    @Schema(description = "The business name")
    private String businessName;

    @ExcelColumn("Address")
    @Schema(description = "The address line")
    private String address;

    @ExcelColumn("City")
    @Schema(description = "The city name")
    private String city;

    @ExcelColumn("County")
    @Schema(description = "The county name")
    private String county;

    @ExcelColumn("State")
    @Schema(description = "The State code")
    private String state;

    @ExcelColumn("Zip")
    @Schema(description = "The zip code")
    private String zip;


    @ExcelColumn("Phone Number")
    @Schema(description = "The phone number")
    private String phoneNumber;

    @ExcelColumn("Issue Date")
    @Schema(description = "The issue date")
    private String issueDate;

    @ExcelColumn("Expiration Date")
    @Schema(description = "The Expiration date")
    private String expirationDate;


    @ExcelColumn("Last Updated")
    @Schema(description = "The last updated date")
    private String lastUpdated;

    @Schema(description = "The latitude in degrees")
    private Double geoLat;
    @Schema(description = "The last updated date")
    private Double geoLng;

    @ExcelColumn("Data Source")
    @Schema(description = "The data source")
    private String dataSource;

    @ExcelColumn("Classification")
    @Schema(description = "The classification")
    private String classification;

    @ExcelColumn("Status")
    @Schema(description = "The status")
    private String status;

    @Schema(description = "The classification array")
    private List<String> classificationArray;

    @Schema(description = "The created at")
    private Timestamp createdAt;

    @Schema(description = "The updated at")
    private Timestamp updatedAt;

    @Schema(description = "The created by")
    private String createdBy;

    @Schema(description = "The updated by")
    private String updatedBy;

    @Schema(description = "The distance to given gps point")
    private BigDecimal distance;
}
