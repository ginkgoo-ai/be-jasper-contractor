package com.jasper.core.contractor.jpa.query;

import lombok.Data;

@Data
public class SortRequest {
    /**
     * Order by
     */

    protected String sortField;

    /**
     * Order type
     */

    protected OrderType sortDirection;
}
