/*
 * Copyright (c) All rights Reserved, Designed By Willie Chen
 *
 * @author: Willie Chen
 * @date:   2024/8/23 09:38
 * Note: this content is limited to internal circulation of the company and is not allowed to be leaked or used for other commercial purposes
 */

package com.jasper.core.contractor.jpa.support;


import com.ginkgooai.core.common.exception.ResourceNotFoundException;
import com.jasper.core.contractor.domain.BaseAuditableEntity;
import com.jasper.core.contractor.jpa.PredicateSupplier;
import com.jasper.core.contractor.jpa.query.PageableHelper;
import com.jasper.core.contractor.jpa.query.PaginationRequest;
import com.jasper.core.contractor.jpa.query.QueryableRequest;
import com.jasper.core.contractor.jpa.query.SortRequest;
import com.jasper.core.contractor.utils.StringTools;
import com.jasper.core.contractor.utils.TypeUtils;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.Resource;
import jakarta.persistence.criteria.Predicate;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * Abstract JPA service
 *
 * @param <E> Entity Type
 * @param <V> View Type
 * @author Willie Chen
 */
@Slf4j
public abstract class AbstractJpaService<E extends BaseAuditableEntity, V extends BaseAuditableEntity, C, U, ER extends AbstractRepository<E, String>, VR extends AbstractRepository<V, String>> implements JpaService<E, V, C, U> {

    /**
     * Entity repository
     */
    protected ER entityRepository;

    /**
     * View repository
     */
    protected VR viewRepository;


    /**
     * The entity class
     */
    protected Class<E> entityClass;

    /**
     * The view class
     */
    protected Class<V> viewClass;

    @Lazy
    @Resource
    protected ApplicationContext applicationContext;


    /**
     * Default constructor
     */
    @SuppressWarnings("ALL")
    @PostConstruct
    public void init() {
        Type entityType = ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0];
        Type viewType = ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[1];
        this.entityClass = (Class<E>) TypeUtils.getClass(entityType);
        this.viewClass = (Class<V>) TypeUtils.getClass(viewType);
        viewRepository = (VR) applicationContext.getBean(StringTools.firstCharToLowerCase(viewClass.getSimpleName()) + "Repository");
        entityRepository = (ER) applicationContext.getBean(StringTools.firstCharToLowerCase(entityClass.getSimpleName()) + "Repository");

    }

    @Override
    public E findById(String id) {
        Optional<E> optional = entityRepository.findById(id);
        if (optional.isEmpty()) {
            throw new IllegalArgumentException(String.format("%s with id %s was not found", entityClass.getSimpleName(), id));
        }
        return optional.get();
    }

    @Override
    public V findViewById(String id) {
        Optional<V> optional = viewRepository.findById(id);
        if (optional.isEmpty()) {
            throw new IllegalArgumentException(String.format("%s with id %s was not found", entityClass.getSimpleName(), id));
        }
        return optional.get();
    }

    @Override
    public Optional<E> findOptionalById(String id) {
        return entityRepository.findById(id);
    }

    @Override
    public E save(E entity) {

        return entityRepository.save(entity);
    }


    @SuppressWarnings("ALL")
    @Override
    public E create(C request) {
        ParameterizedType parameterizedType = (ParameterizedType) this.getClass().getGenericSuperclass();
        Type modelType = parameterizedType.getActualTypeArguments()[0];
        Class<E> entityClass = (Class<E>) TypeUtils.getClass(modelType);
        E entity = TypeUtils.newInstance(entityClass);
        BeanUtils.copyProperties(request, entity);
        entity.setCreatedAt(LocalDateTime.now());


        save(entity);
        return entity;
    }


    @Override
    public E update(U request, String id) {
        E entity = entityRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException(entityClass.getSimpleName(), "id", id));
        Field[] requestFields = request.getClass().getDeclaredFields();
        for (Field sourceField : requestFields) {
            ReflectionUtils.makeAccessible(sourceField);
            Object value = ReflectionUtils.getField(sourceField, request);
            Field targetField = ReflectionUtils.findField(entity.getClass(), sourceField.getName());
            if (value != null && targetField != null && Objects.equals(sourceField.getType(), targetField.getType())) {
                ReflectionUtils.makeAccessible(targetField);
                ReflectionUtils.setField(targetField, entity, value);
            }
        }
        entity.setUpdatedAt(LocalDateTime.now());

        entity = save(entity);

        return entity;
    }

    @Override
    public List<E> saveAll(Iterable<E> entities) {
        return entityRepository.saveAll(entities);
    }

    @Override
    public List<E> findAll() {
        return entityRepository.findAll();
    }

    public Page<E> findAll(Pageable pageable) {
        return entityRepository.findAll(pageable);
    }

    @Override
    public Page<E> findAll(Pageable pageable, Specification<E> specification) {
        return entityRepository.findAll(specification, pageable);
    }

    @Override
    public Page<E> findAll(Pageable pageable, PredicateSupplier<E> supplier) {
        return entityRepository.findAll(pageable, supplier);
    }

    @Override
    public List<E> findAll(PredicateSupplier<E> supplier) {
        return entityRepository.findAll(supplier);
    }

    @Override
    public List<E> findAll(PredicateSupplier<E> supplier, SortRequest sortRequest) {
        Sort sort = PageableHelper.getSort(entityClass, sortRequest);
        return entityRepository.findAll(supplier, sort);
    }

    @Override
    public List<V> findAllView(PredicateSupplier<V> supplier, SortRequest sortRequest) {
        Sort sort = PageableHelper.getSort(viewClass, sortRequest);
        return viewRepository.findAll(supplier, sort);
    }

    @Override
    public List<E> findAllById(Iterable<String> ids) {
        return entityRepository.findAllById(ids);
    }

    @Override
    public long count() {
        return viewRepository.count();
    }

    @Override
    public void deleteById(String id) {
        entityRepository.deleteById(id);
    }

    @Override
    public void delete(E entity) {
        entityRepository.delete(entity);
    }

    @Override
    public void deleteAllById(Iterable<String> ids) {
        entityRepository.deleteAllById(ids);
    }

    @Override
    public void deleteAll(Iterable<E> entities) {
        entityRepository.deleteAll(entities);
    }

    @Override
    @SuppressWarnings("ALL")
    public Page<V> pagination(QueryableRequest<V> queryableRequest, PaginationRequest paginationRequest, SortRequest sortRequest) {

        Pageable pageable = PageableHelper.getPageable(entityClass, paginationRequest, sortRequest);
        Page<V> pageData = viewRepository.findAll(pageable, builder -> {
            Predicate[] customizerPredicates = queryableRequest.getPredicate(builder);
            return builder.and(Arrays.stream(customizerPredicates).filter(Objects::nonNull).toArray(Predicate[]::new));
        });
        return pageData;
    }

    @Override
    public Optional<E> findOne(PredicateSupplier<E> supplier) {
        List<E> list = findAll(supplier);
        if (list.isEmpty()) {
            return Optional.empty();
        } else {
            return Optional.of(list.get(0));
        }
    }


}
