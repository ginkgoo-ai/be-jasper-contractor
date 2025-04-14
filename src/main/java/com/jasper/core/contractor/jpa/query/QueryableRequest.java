/*
 * Copyright (c) All rights Reserved, Designed By Willie Chen
 *
 * @author: Willie Chen
 * @date:   2024/8/23 09:38
 * Note: this content is limited to internal circulation of the company and is not allowed to be leaked or used for other commercial purposes
 */

package com.jasper.core.contractor.jpa.query;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.jasper.core.contractor.jpa.CriteriaBuilderDelegate;
import com.jasper.core.contractor.utils.PredicateUtils;
import io.swagger.v3.oas.annotations.Hidden;
import jakarta.persistence.criteria.Predicate;

import java.util.ArrayList;
import java.util.List;

public interface QueryableRequest<T> {

    @Hidden
    @JsonIgnore
    default Predicate[] getPredicate(CriteriaBuilderDelegate<T> builder) {
        List<Predicate> predicates = new ArrayList<>();

        predicates.addAll(PredicateUtils.buildPredicates(this, builder));
        return predicates.toArray(Predicate[]::new);
    }
}
