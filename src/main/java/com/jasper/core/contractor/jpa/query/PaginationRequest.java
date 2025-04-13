/*
 * Copyright (c) All rights Reserved, Designed By Willie Chen
 *
 * @author: Willie Chen
 * @date:   2024/8/23 09:38
 * Note: this content is limited to internal circulation of the company and is not allowed to be leaked or used for other commercial purposes
 */

package com.jasper.core.contractor.jpa.query;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class PaginationRequest {

    /**
     * Page No
     */
    @NotNull
    @Schema(description = "The page number")
    @Min(1)
    protected Integer pageNo = 1;

    /**
     * Page size
     */
    @Max(500)
    @Min(1)
    @NotNull
    @Schema(description = "The page size")
    protected Integer pageSize = 10;

}
