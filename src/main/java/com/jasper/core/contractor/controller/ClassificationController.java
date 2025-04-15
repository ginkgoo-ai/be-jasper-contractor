package com.jasper.core.contractor.controller;

import com.jasper.core.contractor.domain.classification.Classification;
import com.jasper.core.contractor.service.classification.ClassificationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class ClassificationController {

    private final ClassificationService classificationService;

    @Operation(summary = "Query classifications", description = "Retrieves all classifications")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Query successfully")
    })

    @GetMapping("/classifications")
    public List<Classification> queryAll() {
        return classificationService.queryAll();
    }
}
