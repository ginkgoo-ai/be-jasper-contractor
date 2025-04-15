package com.jasper.core.contractor.controller;

import com.jasper.core.contractor.domain.contractor.Contractor;
import com.jasper.core.contractor.dto.request.QueryContractorRequest;
import com.jasper.core.contractor.jpa.query.PaginationRequest;
import com.jasper.core.contractor.jpa.query.SortRequest;
import com.jasper.core.contractor.service.contractor.ContractorService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Tag(name = "Contractor Management", description = "APIs for managing contractors")
public class ContractorController {

    private final ContractorService contractorService;

    @Operation(summary = "Query contractors", description = "Retrieves a paginated list of contractors with optional filtering by address (fuzzy search), city, state, and sorting by updated dat ")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Query successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input")
    })
    @GetMapping("/contractors")
    public Page<Contractor> query(@ParameterObject QueryContractorRequest queryContractorRequest,
                                  @ParameterObject PaginationRequest paginationRequest,
                                  @ParameterObject SortRequest sortRequest) {
        return contractorService.pagination(queryContractorRequest, paginationRequest, sortRequest);
    }

}
