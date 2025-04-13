/*
 * Copyright (c) All rights Reserved, Designed By Willie Chen
 *
 * @author: Willie Chen
 * @date:   2024/8/23 09:38
 * Note: this content is limited to internal circulation of the company and is not allowed to be leaked or used for other commercial purposes
 */

package com.jasper.core.contractor.jpa.support;

import com.jasper.core.contractor.jpa.PredicateSupplier;
import com.jasper.core.contractor.jpa.query.PageResult;
import com.jasper.core.contractor.jpa.query.PaginationRequest;
import com.jasper.core.contractor.jpa.query.QueryableRequest;
import com.jasper.core.contractor.jpa.query.SortRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import java.util.List;
import java.util.Optional;

/**
 * JPA Service
 *
 * @param <E> Entity Type
 * @param <V> View Type
 */
public interface JpaService<E, V, C, U> {
    E findById(Long id);

    V findViewById(Long id);


    Optional<E> findOptionalById(Long id);

    Optional<E> findOne(PredicateSupplier<E> supplier);

    E save(E entity);

    E create(C request);

    E update(U request, Long id);

    List<E> saveAll(Iterable<E> entities);

    List<E> findAll();

    Page<E> findAll(Pageable pageable);

    Page<E> findAll(Pageable pageable, Specification<E> specification);

    Page<E> findAll(Pageable pageable, PredicateSupplier<E> supplier);

    List<E> findAll(PredicateSupplier<E> supplier);

    List<E> findAll(PredicateSupplier<E> supplier, SortRequest sortRequest);

    List<V> findAllView(PredicateSupplier<V> supplier, SortRequest sortRequest);


    List<E> findAllById(Iterable<Long> ids);

    long count();

    void deleteById(Long id);

    void delete(E entity);

    void deleteAllById(Iterable<Long> ids);

    void deleteAll(Iterable<E> entities);

    PageResult<V> pagination(QueryableRequest<V> queryableRequest, PaginationRequest paginationRequest, SortRequest sortRequest, String keyword);

    void checkRelatedRecordBeforeDeleted(List<Long> idList);

}
