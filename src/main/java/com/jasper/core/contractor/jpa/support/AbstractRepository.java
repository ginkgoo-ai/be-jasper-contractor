/*
 * Copyright (c) All rights Reserved, Designed By Willie Chen
 *
 * @author: Willie Chen
 * @date:   2024/8/23 09:38
 * Note: this content is limited to internal circulation of the company and is not allowed to be leaked or used for other commercial purposes
 */

package com.jasper.core.contractor.jpa.support;


import com.jasper.core.contractor.jpa.CriteriaBuilderDelegate;
import com.jasper.core.contractor.jpa.PredicateSupplier;
import jakarta.persistence.criteria.Predicate;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.NoRepositoryBean;

import java.util.List;
import java.util.Optional;

/**
 * Base Repository
 *
 * @param <T> Entity Type
 * @param <K> Entity ID Type
 * @author Willie Chen
 */
@NoRepositoryBean
public interface AbstractRepository<T, K> extends JpaRepository<T, K>, JpaSpecificationExecutor<T> {

    default Specification<T> buildSpecification(@NotNull PredicateSupplier<T> supplier) {
        return (root, query, builder) -> {
            CriteriaBuilderDelegate<T> delegate = new CriteriaBuilderDelegate<>(root, builder);
            Predicate predicate = supplier.get(delegate);
            if (predicate == null) {
                return null;
            } else {
                return query.where(predicate).getRestriction();
            }

        };
    }

    /**
     * Returns all entities matching the given {@link PredicateSupplier}.
     *
     * @param supplier PredicateSupplier
     * @return never {@literal null}.
     */
    default List<T> findAll(@NotNull PredicateSupplier<T> supplier) {
        return findAll(buildSpecification(supplier));
    }

    default Optional<T> findOne(@NotNull PredicateSupplier<T> supplier) {
        return findOne(buildSpecification(supplier));
    }

    /**
     * Returns all entities matching the given {@link PredicateSupplier}.
     *
     * @param supplier PredicateSupplier
     * @param sort     Sort
     * @return never {@literal null}.
     */
    default List<T> findAll(@NotNull PredicateSupplier<T> supplier, Sort sort) {
        return findAll(buildSpecification(supplier), sort);
    }

    /**
     * Returns a {@link Page} of entities matching the given {@link PredicateSupplier}.
     *
     * @param supplier must not be {@literal null}.
     * @param pageable must not be {@literal null}.
     * @return never {@literal null}.
     */
    default Page<T> findAll(Pageable pageable, @NotNull PredicateSupplier<T> supplier) {

        return findAll(buildSpecification(supplier), pageable);
    }

    default long delete(@NotNull PredicateSupplier<T> supplier) {
        return delete(buildSpecification(supplier));
    }

}
