package com.jasper.core.contractor.controller;

import brave.http.HttpServerResponse;
import com.jasper.core.contractor.domain.contractor.Contractor;
import com.jasper.core.contractor.dto.ResponseFormat;
import com.jasper.core.contractor.dto.request.QueryContractorRequest;
import com.jasper.core.contractor.domain.contractor.ContractorQueryResult;
import com.jasper.core.contractor.dto.response.ContractorDetail;
import com.jasper.core.contractor.dto.response.GeoLocation;
import com.jasper.core.contractor.jpa.query.PaginationRequest;
import com.jasper.core.contractor.jpa.query.SortRequest;
import com.jasper.core.contractor.repository.ContractorRepository;
import com.jasper.core.contractor.service.contractor.ContractorService;
import com.jasper.core.contractor.service.geocoding.GeocodingService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.flywaydb.core.internal.util.JsonUtils;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequiredArgsConstructor
@Tag(name = "Contractor Management", description = "APIs for managing contractors")
public class ContractorController {

    private final ContractorService contractorService;
    private final GeocodingService geocodingService;

    @Operation(summary = "Query contractors", description = "Retrieves a paginated list of contractors with optional filtering by address city, state, and sorting by updated date ")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Query successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid request content")
    })
    @GetMapping("/contractors")
    public Page<ContractorDetail> query(@ParameterObject QueryContractorRequest queryContractorRequest,
                                        @ParameterObject PaginationRequest paginationRequest,
                                        @ParameterObject SortRequest sortRequest) {
        String address = queryContractorRequest.getAddress();
        if(address!=null) {
            GeoLocation geoLocation = geocodingService.geocode(address).orElseThrow(() -> new IllegalArgumentException("Invalid address"));
            queryContractorRequest.setLatitude(geoLocation.getLat());
            queryContractorRequest.setLongitude(geoLocation.getLng());
        }else if(queryContractorRequest.getRadius() != null ) {
            throw new IllegalArgumentException("The address is required when the radius is not null");
        }
        return contractorService.queryPage(queryContractorRequest, paginationRequest, sortRequest);
    }

    @Operation(summary = "Export contractors", description = "Export contractors with optional filtering by address city, state, and sorting by updated date ")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Export successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid request content")
    })
    @GetMapping("/contractors/export")
    public void export(@ParameterObject QueryContractorRequest queryContractorRequest,
                       @ParameterObject SortRequest sortRequest,
                       @RequestParam(defaultValue = "CSV") ResponseFormat format,
                       HttpServletResponse response) throws IOException {

        contractorService.export(queryContractorRequest,sortRequest,format,response);
    }


}
