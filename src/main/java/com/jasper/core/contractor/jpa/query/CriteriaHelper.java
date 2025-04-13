/*
 * Copyright (c) All rights Reserved, Designed By Willie Chen
 *
 * @author: Willie Chen
 * @date:   2024/8/23 09:38
 * Note: this content is limited to internal circulation of the company and is not allowed to be leaked or used for other commercial purposes
 */

package com.jasper.core.contractor.jpa.query;


import com.jasper.core.contractor.jpa.query.impl.AttributeCriteria;
import com.jasper.core.contractor.jpa.support.LambdaMeta;
import com.jasper.core.contractor.jpa.support.LambdaUtils;
import com.jasper.core.contractor.jpa.support.PropertyNamer;
import com.jasper.core.contractor.jpa.support.SFunction;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;

import java.util.List;

/**
 * Criteria helper
 *
 * @param <T> Model Type
 * @author Willie Chen
 */
@Setter
public class CriteriaHelper<T> {
    private String attribute;

    public CriteriaHelper(SFunction<T, ?> attribute) {
        this.attribute = getFieldName(attribute);
    }

    private String getFieldName(SFunction<T, ?> attribute) {
        LambdaMeta meta = LambdaUtils.extract(attribute);
        return PropertyNamer.methodToProperty(meta.getImplMethodName());
    }

    public Criteria eq(Object value) {
        if (value == null) {
            return null;
        }
        return new AttributeCriteria(attribute, Criteria.Operator.EQUAL, value);
    }

    public <V extends Comparable<? super V>> Criteria gte(V value) {
        if (value == null) {
            return null;
        }
        return new AttributeCriteria(attribute, Criteria.Operator.GREATER_THAN_OR_EQUAL, value);
    }

    public <V extends Comparable<? super V>> Criteria gt(V value) {
        if (value == null) {
            return null;
        }
        return new AttributeCriteria(attribute, Criteria.Operator.GREATER_THAN, value);
    }

    public <V extends Comparable<? super V>> Criteria lt(V value) {
        if (value == null) {
            return null;
        }
        return new AttributeCriteria(attribute, Criteria.Operator.LESS_THAN, value);
    }

    public <V extends Comparable<? super V>> Criteria lte(V value) {
        if (value == null) {
            return null;
        }
        return new AttributeCriteria(attribute, Criteria.Operator.LESS_THAN_OR_EQUAL, value);
    }

    public Criteria isTrue() {

        return new AttributeCriteria(attribute, Criteria.Operator.IS_TRUE, null);
    }

    public Criteria isFalse() {
        return new AttributeCriteria(attribute, Criteria.Operator.IS_FALSE, null);
    }

    public Criteria isNull() {

        return new AttributeCriteria(attribute, Criteria.Operator.IS_NULL, null);
    }

    public Criteria isNotNull() {
        return new AttributeCriteria(attribute, Criteria.Operator.NOT_NULL, null);
    }

    public <V extends Comparable<? super V>> Criteria between(V v1, V v2) {
        if (v1 == null || v2 == null) {
            return null;
        }
        Comparable<?>[] args = new Comparable[]{v1, v2};
        return new AttributeCriteria(attribute, Criteria.Operator.BETWEEN, args);
    }

    public Criteria ne(Object value) {
        return notEqual(value);
    }

    public Criteria notEqual(Object value) {
        if (value == null) {
            return null;
        }
        return new AttributeCriteria(attribute, Criteria.Operator.NOT_EQUAL, value);
    }

    public Criteria in(List<?> list) {
        if (list == null || list.isEmpty()) {
            return null;
        }
        return new AttributeCriteria(attribute, Criteria.Operator.IN, list);
    }

    public Criteria in(Object... args) {
        if (args == null || args.length == 0) {
            return null;
        }
        return new AttributeCriteria(attribute, Criteria.Operator.IN, args);
    }

    public Criteria like(String pattern) {
        if (StringUtils.isBlank(pattern)) {
            return null;
        }
        return new AttributeCriteria(attribute, Criteria.Operator.LIKE, pattern);
    }

}
