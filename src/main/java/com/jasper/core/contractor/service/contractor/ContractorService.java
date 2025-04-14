package com.jasper.core.contractor.service.contractor;

import com.jasper.core.contractor.domain.classification.Classification;
import com.jasper.core.contractor.domain.contractor.Contractor;
import com.jasper.core.contractor.domain.contractor.ContractorClassification;
import com.jasper.core.contractor.dto.request.CreateContractorRequest;
import com.jasper.core.contractor.dto.request.QueryContractorRequest;
import com.jasper.core.contractor.dto.request.UpdateContractorRequest;
import com.jasper.core.contractor.dto.response.CslbContractor;
import com.jasper.core.contractor.jpa.query.PageableHelper;
import com.jasper.core.contractor.jpa.query.PaginationRequest;
import com.jasper.core.contractor.jpa.query.QueryableRequest;
import com.jasper.core.contractor.jpa.query.SortRequest;
import com.jasper.core.contractor.jpa.support.AbstractJpaService;
import com.jasper.core.contractor.jpa.support.JpaService;
import com.jasper.core.contractor.repository.ClassificationRepository;
import com.jasper.core.contractor.repository.ContractorClassificationRepository;
import com.jasper.core.contractor.repository.ContractorRepository;
import com.jasper.core.contractor.service.cslb.FetchDataTask;
import com.jasper.core.contractor.utils.ExcelBuilder;
import com.jasper.core.contractor.utils.ForkJoinUtils;
import jakarta.persistence.criteria.Predicate;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Sheet;
import org.flywaydb.core.internal.util.JsonUtils;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.io.FileOutputStream;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class ContractorService  extends AbstractJpaService<Contractor,Contractor, CreateContractorRequest,UpdateContractorRequest,ContractorRepository,ContractorRepository> {

    private final ClassificationRepository classificationRepository;

    private final ContractorRepository contractorRepository;

    private final ContractorClassificationRepository contractorClassificationRepository;

    private final ForkJoinUtils forkJoinUtils;

    @Override
    public Page<Contractor> pagination(QueryableRequest<Contractor> queryableRequest, PaginationRequest paginationRequest, SortRequest sortRequest) {
        QueryContractorRequest queryContractorRequest=(QueryContractorRequest) queryableRequest;

        Pageable pageable = PageableHelper.getPageable(entityClass, paginationRequest, sortRequest);
        return viewRepository.findAll(pageable, builder -> {
            Predicate[] customizerPredicates = queryableRequest.getPredicate(builder);
            return builder.and(Arrays.stream(customizerPredicates).filter(Objects::nonNull).toArray(Predicate[]::new));
        });
    }

    @Async
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
