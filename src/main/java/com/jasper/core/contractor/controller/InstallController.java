package com.jasper.core.contractor.controller;

import com.jasper.core.contractor.service.contractor.ContractorService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class InstallController {

    private final ContractorService contractorService;

    @GetMapping("/install")
    public ResponseEntity<String> install(@RequestParam(defaultValue = "false") boolean clearData) {
        contractorService.sync(clearData);
        return ResponseEntity.ok().body("Installer running...");
    }
}
