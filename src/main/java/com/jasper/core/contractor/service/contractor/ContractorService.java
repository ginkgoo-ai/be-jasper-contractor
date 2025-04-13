package com.jasper.core.contractor.service.contractor;

import com.jasper.core.contractor.domain.classification.Classification;
import com.jasper.core.contractor.domain.contractor.Contractor;
import com.jasper.core.contractor.domain.contractor.ContractorClassification;
import com.jasper.core.contractor.dto.response.CslbContractor;
import com.jasper.core.contractor.repository.ClassificationRepository;
import com.jasper.core.contractor.repository.ContractorClassificationRepository;
import com.jasper.core.contractor.repository.ContractorRepository;
import com.jasper.core.contractor.service.cslb.FetchDataTask;
import com.jasper.core.contractor.utils.ForkJoinUtils;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class ContractorService {

    private final ClassificationRepository classificationRepository;

    private final ContractorRepository contractorRepository;

    private final ContractorClassificationRepository contractorClassificationRepository;

    private final ForkJoinUtils forkJoinUtils;



    @Transactional
    public void sync(){
        List<Classification> classificationList=classificationRepository.findAll();

        long start=System.currentTimeMillis();
        FetchDataTask task = new FetchDataTask(classificationList);
        log.info("Ready to sync contractor,total {} classifications", classificationList.size());
        List<CslbContractor> cslbContractorList = forkJoinUtils.execute(task);
        long duration=System.currentTimeMillis()-start;
        log.info("Total receive {} rows,cost time：{}s",classificationList.size(), TimeUnit.MILLISECONDS.toSeconds(duration));

        cslbContractorList=cslbContractorList.stream().distinct().toList();
        final int total=cslbContractorList.size();
        log.info("Total {} contractors need sync.",total);

        start=System.currentTimeMillis();
        UpdateCounter counter=new UpdateCounter(total);
        UpdateTask updateTask=new UpdateTask(cslbContractorList,counter);
        List<Contractor> contractorList = forkJoinUtils.execute(updateTask);
        log.info("Ready save to database");
        contractorRepository.saveAll(contractorList);
        log.info("Save contractor finished, cost {}ms",(System.currentTimeMillis()-start));

        start=System.currentTimeMillis();
        Set<ContractorClassification> contractorClassificationList = parseContractorClassifications(contractorList);
        contractorClassificationRepository.saveAll(contractorClassificationList);
        log.info("Save contractor classifications finished, cost {}ms",(System.currentTimeMillis()-start));
    }

    @NotNull
    private static Set<ContractorClassification> parseContractorClassifications(List<Contractor> contractorList) {
        Set<ContractorClassification> contractorClassificationList=new HashSet<>();
        for(Contractor contractor: contractorList){
            String[] classificationIds=contractor.getClassification().split("\\|");
            for(String classificationId:classificationIds){
                ContractorClassification contractorClassification=new ContractorClassification();
                contractorClassification.setContractorId(contractor.getId());
                contractorClassification.setClassificationId(classificationId.trim());
                contractorClassificationList.add(contractorClassification);
            }
        }
        return contractorClassificationList;
    }


}
