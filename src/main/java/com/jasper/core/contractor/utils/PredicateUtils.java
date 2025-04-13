/*
 * Copyright (c) All rights Reserved, Designed By Willie Chen
 *
 * @author: Willie Chen
 * @date:   2024/8/23 09:38
 * Note: this content is limited to internal circulation of the company and is not allowed to be leaked or used for other commercial purposes
 */

package com.jasper.core.contractor.utils;

import com.jasper.core.contractor.jpa.CriteriaBuilderDelegate;
import jakarta.persistence.criteria.Predicate;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public class PredicateUtils {
    private PredicateUtils() {

    }

    public static <T> List<Predicate> buildPredicates(Object request, CriteriaBuilderDelegate<T> it) {
        List<Predicate> predicates = new ArrayList<>();
        Field[] fields = request.getClass().getDeclaredFields();
        for (Field field : fields) {
            ReflectionUtils.makeAccessible(field);
            Object value = ReflectionUtils.getField(field, request);
            if (value != null) {
                if (value instanceof String) {
                    predicates.add(it.when(field.getName()).like("%" + value + "%"));
                }
                if (value instanceof List<?> valueList) {
                    predicates.add(it.when(field.getName()).in(valueList));
                } else {
                    predicates.add(it.when(field.getName()).eq(value));
                }
            }
        }
        return predicates;
    }

    public static Predicate[] buildPredicates(Class<?> clazz, CriteriaBuilderDelegate<?> it, String keyword) {
        return getQueryableFieldName(clazz)
                .stream()
                .map(fieldName -> it.when(fieldName).like("%" + keyword + "%"))
                .toArray(Predicate[]::new);

    }

    private static List<String> getQueryableFieldName(Class<?> clazz) {
        List<String> fieldList = new ArrayList<>();
        Field[] fields = clazz.getDeclaredFields();
        for (Field field : fields) {
            if (field.getType().equals(String.class)) {
                fieldList.add(field.getName());
            }
        }
        return fieldList;
    }
}
