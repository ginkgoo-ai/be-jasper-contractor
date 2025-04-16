package com.jasper.core.contractor.dto.request;


import com.jasper.core.contractor.domain.contractor.Contractor;
import com.jasper.core.contractor.jpa.query.QueryableRequest;
import io.swagger.v3.oas.annotations.Hidden;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.annotation.Nullable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "Request parameter for query contractors")
public class QueryContractorRequest implements QueryableRequest<Contractor> {

    @Schema(description = "The contractor's address")
    private String address;

    @Schema(description = "The contractor's locality")
    @Nullable
    private String city;


    @Schema(description = "The contractor's State code, not required")
    @Nullable
    private String state;

    @Schema(description = "The contractor's license number, not required")
    @Nullable
    private String licenseNumber;


    @Schema(description = "The contractor's classification, not required")
    private List<String> classifications;

    @Schema(description = "The region to search specified as a circle, defined by center point and radius in meters.")
    @Nullable
    private Double radius;

    @Hidden
    private Double latitude;

    @Hidden
    private Double longitude;


}
