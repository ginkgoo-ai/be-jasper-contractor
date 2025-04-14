package com.jasper.core.contractor.jpa.query;

import lombok.Data;

@Data
public class SortRequest {
    /**
     * Order by
     */

    protected String orderBy;

    /**
     * Order type
     */

    protected OrderType orderType;
}
