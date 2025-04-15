package com.jasper.core.contractor.service.classification;

import com.jasper.core.contractor.domain.classification.Classification;
import com.jasper.core.contractor.repository.ClassificationRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class ClassificationService {
    private final ClassificationRepository classificationRepository;

    public List<Classification> queryAll() {
        return classificationRepository.findAll();
    }
}
