package com.jasper.core.contractor.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@Schema(description = "Brief information about talent's application")
public class ApplicationBriefResponse {
    @Schema(description = "Application ID", example = "app_123")
    private String id;
}