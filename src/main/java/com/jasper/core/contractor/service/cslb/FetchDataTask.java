package com.jasper.core.contractor.service.cslb;

import com.jasper.core.contractor.domain.classification.Classification;
import com.jasper.core.contractor.dto.response.CslbContractor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.RecursiveTask;

@Slf4j
public class FetchDataTask extends RecursiveTask<List<CslbContractor>> {
    private final List<Classification> classificationList;

    public FetchDataTask(List<Classification> classificationList) {
        this.classificationList = classificationList;
    }

    @Override
    protected List<CslbContractor> compute() {

        if (CollectionUtils.isEmpty(classificationList)) {
            return List.of();
        } else if (classificationList.size() > 10) {
            int middle = classificationList.size() / 2;
            FetchDataTask left = new FetchDataTask(classificationList.subList(0, middle));
            FetchDataTask right = new FetchDataTask(classificationList.subList(middle, classificationList.size()));
            left.fork();
            right.fork();

            List<CslbContractor> all = new ArrayList<>();
            all.addAll(left.join());
            all.addAll(right.join());
            return all;
        } else {
            return sync(classificationList);
        }
    }

    private List<CslbContractor> sync(List<Classification> classificationList) {

        try (CslbClient client = new CslbClient()) {
            List<String> classificationCodeList = classificationList.stream().map(Classification::getId).toList();
            log.info("Fetch by classification codes: {}", classificationCodeList);
            List<CslbContractor> result = client.search(classificationCodeList);
            log.info("Fetch result with {}: {}",classificationCodeList, result.size());
            return result;
        } catch (IOException e) {
            log.error("Failed to sync data from remote service", e);
        }
        return List.of();
    }
}
