package com.jasper.core.contractor.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AddressValidationResponse {
    private String responseId;
    private AddressValidationResult result;
}
