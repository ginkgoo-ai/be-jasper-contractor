package com.jasper.core.contractor.dto.response;

import lombok.Data;

import java.util.List;
@Data
public class GoogleGeocodingResponse {
    private String status;
    private List<GoogleGeocodingResult> results;

}
