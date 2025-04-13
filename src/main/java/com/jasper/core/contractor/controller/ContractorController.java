package com.jasper.core.contractor.controller;

import com.jasper.core.contractor.service.contractor.ContractorService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/contractors")
@RequiredArgsConstructor
@Tag(name = "Contractor Management", description = "APIs for managing contractors")
public class ContractorController {

    private final ContractorService contractorService;

    @GetMapping("/init")
    public ResponseEntity<String> init(){

        contractorService.sync();
        return ResponseEntity.ok("OK");
    }

}
