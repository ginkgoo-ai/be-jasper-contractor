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
public class ConvertTask extends RecursiveTask<List<Contractor>> {
    private final List<CslbContractor> cslbContractorList;
    private final AtomCounter counter;

    public ConvertTask(List<CslbContractor> cslbContractorList, AtomCounter counter) {
        this.cslbContractorList = cslbContractorList;
        this.counter = counter;
    }

    @Override
    protected List<Contractor> compute() {

        if (CollectionUtils.isEmpty(cslbContractorList)) {
            return List.of();
        } else if (cslbContractorList.size() > 1) {
            int middle = cslbContractorList.size() / 2;
            ConvertTask left = new ConvertTask(cslbContractorList.subList(0, middle), counter);
            ConvertTask right = new ConvertTask(cslbContractorList.subList(middle, cslbContractorList.size()), counter);
            left.fork();
            right.fork();

            List<Contractor> all = new ArrayList<>();
            all.addAll(left.join());
            all.addAll(right.join());
            return all;
        } else {
            CslbContractor cslbContractor = cslbContractorList.get(0);
            Contractor contractor = saveOrUpdate(cslbContractor);
            counter.incrementAndGet();
            return List.of(contractor);
        }
    }

    private Contractor saveOrUpdate(CslbContractor dto) {
        ContractorRepository contractorRepository = ApplicationContextUtils.get().getBean(ContractorRepository.class);

//        Optional<Contractor> optionalContractor=contractorRepository.findByLicenseNumber(dto.getLicenseNumber());
//        Contractor contractor;
//        if(optionalContractor.isPresent()){
//            contractor=optionalContractor.get();
//            if(!isSame(dto,contractor)){
//                BeanUtils.copyProperties(dto,contractor);
//                updateGeo(contractor);
//            }else{
//                contractorClassificationRepository.deleteByContractorId(contractor.getId());
//            }
//        }else{

        Contractor contractor = new Contractor();
        BeanUtils.copyProperties(dto, contractor);
        contractor.setDataSource("CSLB");
        contractor.setCreatedAt(LocalDateTime.now());
        contractor.setUpdatedAt(LocalDateTime.now());
        if (StringUtils.isNotBlank(contractor.getClassification())) {
            String[] classificationIds = contractor.getClassification().split("\\|");
            contractor.setClassificationArray(Arrays.stream(classificationIds).filter(StringUtils::isNotBlank).map(String::trim).toList());
        }
//            updateGeo(contractor);

        return contractor;
    }


    private void updateGeo(Contractor contractor) {
        GeocodingService googleMapGeocodingProvider = ApplicationContextUtils.get().getBean(GeocodingService.class);

        String address=contractor.getAddress()+", "+contractor.getCity()+", "+contractor.getState();
        Optional<GeoLocation> optionalGeoLocation = googleMapGeocodingProvider.geocode(address);
        if (optionalGeoLocation.isPresent()) {
            GeoLocation geoLocation = optionalGeoLocation.get();
            contractor.setGeoLat(geoLocation.getLat());
            contractor.setGeoLng(geoLocation.getLng());
            //log.info("Parse address: {}  geo:{}",contractor.getAddress(),geoLocation);
        }
    }

    private boolean isSame(CslbContractor dto, Contractor contractor) {
        CslbContractor dto2 = new CslbContractor();
        BeanUtils.copyProperties(contractor, dto2);
        return dto.equals(dto2);
    }
}
