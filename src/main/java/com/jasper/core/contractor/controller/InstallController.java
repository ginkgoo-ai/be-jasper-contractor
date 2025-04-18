package com.jasper.core.contractor.controller;

import com.ginkgooai.core.common.exception.InternalServiceException;
import com.jasper.core.contractor.domain.contractor.Contractor;
import com.jasper.core.contractor.dto.response.GeoLocation;
import com.jasper.core.contractor.handle.InstallFinishedEvent;
import com.jasper.core.contractor.handle.UpdateFinishedEvent;
import com.jasper.core.contractor.service.contractor.ContractorService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
public class InstallController  {

    private final ContractorService contractorService;
    private boolean installing = false;
    private boolean updating=false;

    @Operation(hidden = true)
    @GetMapping("/install")
    public ResponseEntity<String> install(@RequestParam(defaultValue = "false") boolean clearData) throws IOException {
        if(installing){
            return ResponseEntity.badRequest().body("Installer is in progress.");
        }
        installing=true;
        contractorService.sync(clearData);
        return ResponseEntity.ok().body("Installer running...");
    }

    @Operation(hidden = true)
    @GetMapping("/update-location")
    public ResponseEntity<String> updateLocation(@RequestParam(required = false) String county,
                                                 @RequestParam(required = false) String city,
                                                 @RequestParam int targetCount) throws Exception {
        if(updating){
            return ResponseEntity.badRequest().body("Installer is in progress.");
        }
        updating=true;
        List<Contractor> contractorList  = contractorService.query(county, city, targetCount);
        if (contractorList.size() != targetCount) {
            updating=false;
            throw new InternalServiceException("Contractor count mismatch");
        }
        contractorService.updateGeoLocation(contractorList);
        return ResponseEntity.ok().body(targetCount+" records updating...");
    }



    @Component
    public static class InstallerHandler implements ApplicationListener<InstallFinishedEvent> {
        private InstallController controller;
        public InstallerHandler(InstallController controller) {
            this.controller = controller;
        }
        @Override
        public void onApplicationEvent(InstallFinishedEvent event) {
            controller.installing = false;
        }
    }
    @Component
    public static class UpdateHandler implements ApplicationListener<UpdateFinishedEvent> {
        private InstallController controller;
        public UpdateHandler(InstallController controller) {
            this.controller = controller;
        }
        @Override
        public void onApplicationEvent(UpdateFinishedEvent event) {
            controller.updating=false;
        }
    }
}

