package com.jasper.core.contractor.dto.response;

import lombok.Data;

@Data
public class AddressValidationResult {
    private Verdict verdict;
    private GeoCode geocode;
}
