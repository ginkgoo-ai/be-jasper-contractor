package com.jasper.core.contractor.service.contractor;

import com.jasper.core.contractor.domain.contractor.Contractor;
import com.jasper.core.contractor.dto.response.CslbContractor;
import com.jasper.core.contractor.dto.response.GeoLocation;
import com.jasper.core.contractor.repository.ContractorRepository;
import com.jasper.core.contractor.service.geocoding.GeocodingService;
import com.jasper.core.contractor.utils.ApplicationContextUtils;
import com.jasper.core.contractor.utils.AtomCounter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.RecursiveTask;

@Slf4j
public class UpdateGeoTask extends RecursiveTask<List<Contractor>> {
    private final List<Contractor> contractorList;
    private final AtomCounter counter;

    public UpdateGeoTask(List<Contractor> contractorList, AtomCounter counter) {
        this.contractorList = contractorList;
        this.counter = counter;
    }

    @Override
    protected List<Contractor> compute() {

        if (CollectionUtils.isEmpty(contractorList)) {
            return List.of();
        } else if (contractorList.size() > 1) {
            int middle = contractorList.size() / 2;
            UpdateGeoTask left = new UpdateGeoTask(contractorList.subList(0, middle), counter);
            UpdateGeoTask right = new UpdateGeoTask(contractorList.subList(middle, contractorList.size()), counter);
            left.fork();
            right.fork();

            List<Contractor> all = new ArrayList<>();
            all.addAll(left.join());
            all.addAll(right.join());
            return all;
        } else {
            Contractor contractor = contractorList.getFirst();
            updateLocation(contractor);
            counter.incrementAndGet();
            return List.of(contractor);
        }
    }

    private void updateLocation(Contractor contractor) {
        GeocodingService geocodingService = ApplicationContextUtils.get().getBean(GeocodingService.class);

        String address=contractor.getAddress()+", "+contractor.getCity()+", "+contractor.getState();
        Optional<GeoLocation> optionalGeoLocation = geocodingService.geocode(address);
        if (optionalGeoLocation.isPresent()) {
            GeoLocation geoLocation = optionalGeoLocation.get();
            contractor.setGeoLat(geoLocation.getLatitude());
            contractor.setGeoLng(geoLocation.getLongitude());

        }
    }



}
