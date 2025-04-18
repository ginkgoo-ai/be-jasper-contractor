package com.jasper.core.contractor.dto.response;

import lombok.Data;
import org.springframework.boot.convert.DataSizeUnit;

import java.util.List;
@Data
public class GoogleGeocodingResponse {
    private String status;
    private List<GoogleGeocodingResult> results;

}
