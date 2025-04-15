package com.jasper.core.contractor.jpa.query;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class SortRequest {
    /**
     * Sort Field
     */
    @Schema(description = "The sort field",example = "createdAt,updatedAt")
    protected String sortField;

    /**
     * Sort Type
     */
    @Schema(description = "The sort direction",example = "ASC,DESC")
    protected OrderType sortDirection;
}
