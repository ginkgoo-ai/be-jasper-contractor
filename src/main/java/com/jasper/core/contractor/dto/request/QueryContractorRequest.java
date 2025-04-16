package com.jasper.core.contractor.dto.request;


import com.jasper.core.contractor.domain.contractor.Contractor;
import com.jasper.core.contractor.dto.ResponseFormat;
import com.jasper.core.contractor.jpa.CriteriaBuilderDelegate;
import com.jasper.core.contractor.jpa.query.QueryableRequest;
import com.jasper.core.contractor.utils.StringTools;
import io.swagger.v3.oas.annotations.Hidden;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;
import jakarta.persistence.criteria.Predicate;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.flywaydb.core.internal.util.JsonUtils;
import org.springframework.data.repository.query.Param;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "Request parameter for query contractors")
public class QueryContractorRequest implements QueryableRequest<Contractor> {

    @Schema(description = "The contractor's address")
    @NotNull
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
    @NotNull
    @NotEmpty
    private List<String> classifications;

    @Schema(description = "The region to search specified as a circle, defined by center point and radius in meters.")
    @Nullable
    private Double radius;

    @Hidden
    private Double latitude;

    @Hidden
    private Double longitude;


}
