/*
 * Copyright (c) All rights Reserved, Designed By Willie Chen
 *
 * @author: Willie Chen
 * @date:   2024/8/23 09:38
 * Note: this content is limited to internal circulation of the company and is not allowed to be leaked or used for other commercial purposes
 */

package com.jasper.core.contractor.jpa;

import jakarta.persistence.criteria.Predicate;

/**
 * Predicate Supplier
 *
 * @param <T> Entity Type
 * @author Willie Chen
 */
public interface PredicateSupplier<T> {
    Predicate get(CriteriaBuilderDelegate<T> builder);

}
