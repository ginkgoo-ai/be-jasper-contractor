package com.jasper.core.contractor.service.contractor;

import com.fasterxml.jackson.core.type.TypeReference;
import com.ginkgooai.core.common.exception.InternalServiceException;
import com.jasper.core.contractor.domain.classification.Classification;
import com.jasper.core.contractor.domain.contractor.Contractor;
import com.jasper.core.contractor.domain.contractor.ContractorQueryResult;
import com.jasper.core.contractor.dto.ResponseFormat;
import com.jasper.core.contractor.dto.request.CreateContractorRequest;
import com.jasper.core.contractor.dto.request.QueryContractorRequest;
import com.jasper.core.contractor.dto.request.UpdateContractorRequest;
import com.jasper.core.contractor.dto.response.ContractorDetail;
import com.jasper.core.contractor.dto.response.CslbContractor;
import com.jasper.core.contractor.dto.response.GeoLocation;
import com.jasper.core.contractor.handle.InstallFinishedEvent;
import com.jasper.core.contractor.handle.UpdateFinishedEvent;
import com.jasper.core.contractor.jpa.query.PageableHelper;
import com.jasper.core.contractor.jpa.query.PaginationRequest;
import com.jasper.core.contractor.jpa.query.QueryableRequest;
import com.jasper.core.contractor.jpa.query.SortRequest;
import com.jasper.core.contractor.jpa.support.AbstractJpaService;
import com.jasper.core.contractor.repository.ClassificationRepository;
import com.jasper.core.contractor.repository.ContractorRepository;
import com.jasper.core.contractor.service.cslb.CslbClient;
import com.jasper.core.contractor.service.geocoding.GeocodingService;
import com.jasper.core.contractor.utils.ExcelBuilder;
import com.jasper.core.contractor.utils.ForkJoinUtils;
import com.jasper.core.contractor.utils.StringTools;
import com.jasper.core.contractor.utils.TypeUtils;

import jakarta.persistence.EntityManager;
import jakarta.persistence.criteria.Predicate;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.Sheet;

import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.poi.util.IOUtils;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.flywaydb.core.internal.util.JsonUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.context.ApplicationContext;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.io.*;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class ContractorService extends AbstractJpaService<Contractor, Contractor, CreateContractorRequest, UpdateContractorRequest, ContractorRepository, ContractorRepository> {
    private static final String DEFAULT_SORT_FIELD = "distance";

    private final ClassificationRepository classificationRepository;

    private final ContractorRepository contractorRepository;

    private final ForkJoinUtils forkJoinUtils;

    private final ApplicationContext applicationContext;

    private final GeocodingService geocodingService;

    private final EntityManager entityManager;

    public Page<ContractorDetail> queryPage(QueryableRequest<Contractor> queryableRequest, PaginationRequest paginationRequest, SortRequest sortRequest) {
        if (sortRequest.getSortField() == null) {
            sortRequest.setSortField(DEFAULT_SORT_FIELD);
        }
        Pageable pageable = PageableHelper.getPageable(ContractorQueryResult.class, paginationRequest, sortRequest);
        QueryContractorRequest queryContractorRequest = (QueryContractorRequest) queryableRequest;

        List<String> classificationCodeList = queryContractorRequest.getClassifications();
        if(CollectionUtils.isEmpty(classificationCodeList)){
            classificationCodeList=classificationRepository.findAll().stream().map(Classification::getId).toList();
        }

        String[] classificationArray = classificationCodeList.toArray(String[]::new);


        Page<ContractorQueryResult> queryResult = contractorRepository.pagination(queryContractorRequest.getLatitude(),
                queryContractorRequest.getLongitude(),
                queryContractorRequest.getRadius(),
                queryContractorRequest.getCity(),
                queryContractorRequest.getState(),
                queryContractorRequest.getLicenseNumber(),
                classificationArray,
                pageable);
        List<ContractorDetail> records = queryResult.getContent().stream().map(this::convertToVo).toList();
        return new PageImpl<>(records, pageable, queryResult.getTotalElements());
    }

    private ContractorDetail convertToVo(ContractorQueryResult it) {
        ContractorDetail contractorDetail = new ContractorDetail();
        BeanUtils.copyProperties(it, contractorDetail);
        if (it.getClassificationArray() != null) {
            TypeReference<List<String>> ref = new TypeReference<>() {
            };
            contractorDetail.setClassificationArray(JsonUtils.parseJson(it.getClassificationArray(), ref));
        }
        return contractorDetail;
    }

    public void export(QueryContractorRequest queryContractorRequest, SortRequest sortRequest, ResponseFormat format,
                       HttpServletResponse response) throws IOException {
        String address = queryContractorRequest.getAddress();
        GeoLocation geoLocation = geocodingService.geocode(address)
                .orElseThrow(() -> new IllegalArgumentException("Invalid address"));
        queryContractorRequest.setLatitude(geoLocation.getLatitude());
        queryContractorRequest.setLongitude(geoLocation.getLongitude());

        List<ContractorDetail> contractorDetailList = new ArrayList<>();
        PaginationRequest paginationRequest = new PaginationRequest();
        paginationRequest.setPage(1);
        paginationRequest.setSize(100);
        boolean hasNextPage = true;
        while (hasNextPage) {
            Page<ContractorDetail> pageResult = queryPage(queryContractorRequest, paginationRequest, sortRequest);
            contractorDetailList.addAll(pageResult.getContent());
            hasNextPage = pageResult.hasNext();
        }

        String[] columns = TypeUtils.getExportColumns(ContractorDetail.class);
        List<Object[]> rows = TypeUtils.getExportValues(contractorDetailList);

        String downloadFileName = "Contractors." + (ResponseFormat.CSV.equals(format) ? "csv" : "xlsx");
        downloadFileName = URLEncoder.encode(downloadFileName, StandardCharsets.UTF_8);
        downloadFileName = downloadFileName.replace("+", "%20");
        response.setHeader(HttpHeaders.CONTENT_DISPOSITION, "attachment;filename*=UTF-8''" + downloadFileName);
        response.setContentType("application/octet-stream");

        OutputStream out = response.getOutputStream();
        if (ResponseFormat.CSV.equals(format)) {
            try (BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(out, StandardCharsets.UTF_8));
                 CSVPrinter csvPrinter = new CSVPrinter(writer, CSVFormat.DEFAULT
                         .builder().setHeader(columns).get())) {

                for (Object[] row : rows) {
                    csvPrinter.printRecord(row);
                }
                csvPrinter.flush(); // 确保所有数据都被写出
            }
        } else {
            try (ExcelBuilder builder = ExcelBuilder.create()) {

                Sheet sheet = builder.createSheet("Contractors");
                builder.createHeader(sheet, columns);
                int size = rows.size();
                for (int i = 0; i < size; i++) {
                    Object[] row = rows.get(i);
                    builder.createRow(i + 1, sheet, row);
                }
                Object[] footerValues = new Object[columns.length];
                Arrays.fill(footerValues, "");
                builder.createFooter(size + 1, sheet, footerValues);
                builder.save(out);
            }

        }

    }

    @Async
    @Transactional
    public void sync(boolean clearData) throws IOException {
//        List<Classification> classificationList = classificationRepository.findAll();
        if (clearData) {
            long count = contractorRepository.delete(it -> it.when(Contractor::getId).isNotNull());
            log.info("Total {} contractors be deleted", count);
        }
//        long start = System.currentTimeMillis();
//        FetchDataTask task = new FetchDataTask(classificationList);
//        log.info("Ready to sync contractor,total {} classifications", classificationList.size());
//        List<CslbContractor> cslbContractorList = forkJoinUtils.execute(task);
//        long duration = System.currentTimeMillis() - start;
//        log.info("Total receive {} rows,cost time：{}s", classificationList.size(), TimeUnit.MILLISECONDS.toSeconds(duration));
        ClassLoader classLoader = ContractorService.class.getClassLoader();
        IOUtils.setByteArrayMaxOverride(300000000);
        try (CslbClient client = new CslbClient();
             InputStream in = classLoader.getResourceAsStream("all.xlsx");
             XSSFWorkbook workbook = (XSSFWorkbook) WorkbookFactory.create(in)){

            List<CslbContractor> cslbContractorList = client.parseExcel(workbook);
            cslbContractorList = cslbContractorList.stream().distinct().toList();
            final int total = cslbContractorList.size();
            log.info("Total {} contractors need sync.", total);

            long start = System.currentTimeMillis();
            UpdateCounter counter = new UpdateCounter(total);
            ConvertTask convertTask = new ConvertTask(cslbContractorList, counter);
            List<Contractor> contractorList = forkJoinUtils.execute(convertTask);
            log.info("Ready save to database");
            contractorRepository.saveAll(contractorList);
            log.info("Save contractor finished, cost {}ms", (System.currentTimeMillis() - start));

        }finally {
            applicationContext.publishEvent(new InstallFinishedEvent(this));
        }


    }

    public List<Contractor> query(String county, String city, int targetCount) {
        List<Contractor> contractorList = contractorRepository.findAll(it -> {
            List<Predicate> predicates = new ArrayList<>();
            predicates.add(it.when(Contractor::getGeoLat).isNull());
            if (StringUtils.isNotBlank(county)) {
                predicates.add(it.when(Contractor::getCounty).ilike(StringTools.likePattern(county)));
            }
            if (StringUtils.isNotBlank(city)) {
                predicates.add(it.when(Contractor::getCity).ilike(StringTools.likePattern(city)));
            }

            return it.and(predicates.toArray(Predicate[]::new));
        });
        if (contractorList.size() != targetCount) {
            throw new InternalServiceException("Contractor count mismatch");
        }
        return contractorList;
    }

    @Async
    public void updateGeoLocation(List<Contractor> contractorList) {
        int total = contractorList.size();
        long start = System.currentTimeMillis();
        UpdateCounter counter = new UpdateCounter(total);
        UpdateGeoTask updateGeoTask = new UpdateGeoTask(contractorList, counter);
        List<Contractor> result = forkJoinUtils.execute(updateGeoTask);
        log.info("Ready save to database");
        long successCount = result.stream().filter(it -> it.getGeoLat() != null && it.getGeoLng() != null).count();
        long failCount = result.stream().filter(it -> it.getGeoLat() == null || it.getGeoLng() == null).count();
        log.info("Success count: {} ,fail count:{}.", successCount, failCount);
        contractorRepository.saveAll(result);
        log.info("Update contractor finished, cost {}ms", (System.currentTimeMillis() - start));

        applicationContext.publishEvent(new UpdateFinishedEvent(this));
    }

}
