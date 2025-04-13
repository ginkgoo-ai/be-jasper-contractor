/*
 * Copyright (c) All rights Reserved, Designed By Willie Chen
 *
 * @author: Willie Chen
 * @date:   2024/8/23 09:38
 * Note: this content is limited to internal circulation of the company and is not allowed to be leaked or used for other commercial purposes
 */

package com.jasper.core.contractor.jpa.query;


import com.jasper.core.contractor.jpa.query.impl.JunctionCriteria;
import com.jasper.core.contractor.jpa.support.SFunction;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

/**
 * Criteria Query
 *
 * @param <T> Entity Type
 * @author Willie
 */
public class CriteriaQuery<T> {

    public CriteriaHelper<T> when(SFunction<T, ?> attribute) {
        return new CriteriaHelper<>(attribute);
    }

    public Criteria or(Criteria... restrictions) {
        if (restrictions == null || restrictions.length == 0) {
            return null;
        }
        List<Criteria> array = Arrays.stream(restrictions).filter(Objects::nonNull).toList();
        return new JunctionCriteria(Criteria.Operator.OR, array);
    }

    public Criteria and(Criteria... restrictions) {
        if (restrictions == null || restrictions.length == 0) {
            return null;
        }
        List<Criteria> array = Arrays.stream(restrictions).filter(Objects::nonNull).toList();
        return new JunctionCriteria(Criteria.Operator.AND, array);
    }

}
