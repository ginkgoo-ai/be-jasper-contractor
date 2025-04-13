/*
 * Copyright (c) All rights Reserved, Designed By Willie Chen
 *
 * @author: Willie Chen
 * @date:   2024/8/23 09:38
 * Note: this content is limited to internal circulation of the company and is not allowed to be leaked or used for other commercial purposes
 */

package com.jasper.core.contractor.jpa;

import com.jasper.core.contractor.jpa.support.SFunction;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;

import java.util.Arrays;
import java.util.Objects;

/**
 * Criteria Builder Delegate
 *
 * @param <T> Entity Type
 * @author Willie Chen
 */
public class CriteriaBuilderDelegate<T> {
    private final CriteriaBuilder builder;
    private final Root<T> root;

    public CriteriaBuilderDelegate(Root<T> root, CriteriaBuilder builder) {
        this.builder = builder;
        this.root = root;
    }

    public PredicateBuilder<T> when(SFunction<T, ?> attribute) {
        return new PredicateBuilder<>(builder, root, attribute);
    }

    public PredicateBuilder<T> when(String attributeName) {
        PredicateBuilder<T> pb = new PredicateBuilder<>(builder, root, null);
        pb.setAttributeName(attributeName);
        return pb;
    }

    public Predicate or(Predicate... restrictions) {
        if (restrictions == null || restrictions.length == 0) {
            return null;
        }
        Predicate[] array = Arrays.stream(restrictions).filter(Objects::nonNull).toArray(Predicate[]::new);
        if (array.length == 0) {
            return null;
        }
        return builder.or(array);
    }

    public Predicate and(Predicate... restrictions) {
        if (restrictions == null || restrictions.length == 0) {
            return null;
        }
        Predicate[] array = Arrays.stream(restrictions).filter(Objects::nonNull).toArray(Predicate[]::new);
        if (array.length == 0) {
            return null;
        }
        return builder.and(array);
    }
}
