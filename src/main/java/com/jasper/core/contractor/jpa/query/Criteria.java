/*
 * Copyright (c) All rights Reserved, Designed By Willie Chen
 *
 * @author: Willie Chen
 * @date:   2024/8/23 09:38
 * Note: this content is limited to internal circulation of the company and is not allowed to be leaked or used for other commercial purposes
 */

package com.jasper.core.contractor.jpa.query;

import com.jasper.core.contractor.jpa.CriteriaBuilderDelegate;
import com.jasper.core.contractor.jpa.PredicateBuilder;
import com.jasper.core.contractor.jpa.query.impl.AttributeCriteria;
import com.jasper.core.contractor.jpa.query.impl.JunctionCriteria;
import jakarta.persistence.criteria.Predicate;

/**
 * Criteria
 *
 * @author Willie Chen
 */
public interface Criteria {
    /**
     * Get operator
     *
     * @return
     */
    Operator getOperator();

    /**
     * Convert Criteria to Predicate
     *
     * @param builder CriteriaBuilder
     * @param <T>     Entity Type
     * @return Predicate
     */
    @SuppressWarnings("ALL")
    default <T> Predicate toPredicate(CriteriaBuilderDelegate<T> builder) {
        Predicate predicate = null;
        Operator operator = getOperator();
        if (Operator.AND.equals(operator) || Operator.OR.equals(operator)) {
            JunctionCriteria junctionCriteria = (JunctionCriteria) this;
            Predicate[] predicates = junctionCriteria.getExpressions().stream().map(it -> it.toPredicate(builder))
                    .toArray(Predicate[]::new);
            if (Operator.AND.equals(operator)) {
                predicate = builder.and(predicates);
            } else {
                predicate = builder.or(predicates);
            }
        } else {
            AttributeCriteria attributeCriteria = (AttributeCriteria) this;
            Object value = attributeCriteria.getValue();
            PredicateBuilder<T> predicateBuilder = builder.when(attributeCriteria.getAttribute());
            switch (operator) {
                case IN -> predicate = predicateBuilder.in(value);
                case EQUAL -> predicate = predicateBuilder.eq(value);
                case NOT_EQUAL -> predicate = predicateBuilder.ne(value);
                case LESS_THAN -> predicate = predicateBuilder.lt((Comparable) value);
                case LESS_THAN_OR_EQUAL -> predicate = predicateBuilder.lte((Comparable) value);
                case GREATER_THAN -> predicate = predicateBuilder.gt((Comparable) value);
                case GREATER_THAN_OR_EQUAL -> predicate = predicateBuilder.gte((Comparable) value);
                case IS_NULL -> predicate = predicateBuilder.isNull();
                case NOT_NULL -> predicate = predicateBuilder.isNotNull();
                case IS_FALSE -> predicate = predicateBuilder.isFalse();
                case IS_TRUE -> predicate = predicateBuilder.isTrue();
                case LIKE -> predicate = predicateBuilder.like((String) value);
                case BETWEEN -> {
                    Comparable[] args = (Comparable[]) value;
                    predicate = predicateBuilder.between(args[0], args[1]);
                }
                default -> throw new IllegalArgumentException("Invalid operator:" + operator);
            }
        }

        return predicate;
    }

    /**
     * Supported Operations
     */
    enum Operator {
        AND, OR, EQUAL, NOT_EQUAL, LESS_THAN, LESS_THAN_OR_EQUAL, GREATER_THAN, GREATER_THAN_OR_EQUAL, IS_NULL, NOT_NULL,
        BETWEEN, IS_TRUE, IS_FALSE, IN, LIKE

    }

}
