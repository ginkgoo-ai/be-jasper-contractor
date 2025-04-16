/*
 * Copyright (c) All rights Reserved, Designed By Willie Chen
 *
 * @author: Willie Chen
 * @date:   2024/8/23 09:38
 * Note: this content is limited to internal circulation of the company and is not allowed to be leaked or used for other commercial purposes
 */

package com.jasper.core.contractor.utils;


import com.ginkgooai.core.common.exception.InternalServiceException;
import com.jasper.core.contractor.aspect.annotation.ExcelColumn;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.*;
import java.util.ArrayList;
import java.util.List;

public class TypeUtils {
    private TypeUtils() {

    }

    public static Class<?> getClass(Type type) {
        if (type.getClass() == Class.class) {
            return (Class<?>) type;
        }

        if (type instanceof ParameterizedType parameterizedType) {
            return getClass((parameterizedType).getRawType());
        }

        if (type instanceof TypeVariable) {
            Type boundType = ((TypeVariable<?>) type).getBounds()[0];
            if (boundType instanceof Class<?> clzz) {
                return clzz;
            }
            return getClass(boundType);
        }

        if (type instanceof WildcardType wildcardType) {
            Type[] upperBounds = (wildcardType).getUpperBounds();
            if (upperBounds.length == 1) {
                return getClass(upperBounds[0]);
            }
        }

        return Object.class;
    }

    public static <T> T newInstance(Class<T> clzz) {
        try {
            return clzz.getDeclaredConstructor().newInstance();
        } catch (Exception e) {
            throw new InternalServiceException(e.getMessage());
        }

    }

    public static String[] getExportColumns(Class<?> clzz){
        List<String> columns = new ArrayList<>();
        Field[] fields = clzz.getDeclaredFields();
        for(Field field : fields){

            if(field.isAnnotationPresent(ExcelColumn.class)){
                ExcelColumn excelColumn=field.getAnnotation(ExcelColumn.class);
                columns.add(excelColumn.value());
            }
        }
        return columns.toArray(String[]::new);
    }

    public static List<Object[]> getExportValues(List<?> records){
        List<Object[]> rows = new ArrayList<>();
        if(CollectionUtils.isEmpty(records)){
            return rows;
        }
        Class<?> clzz = records.getFirst().getClass();

        Field[] fields = clzz.getDeclaredFields();
        for(Object record : records){
            List<Object> row=new ArrayList<>();
            for(Field field : fields) {
                if(field.isAnnotationPresent(ExcelColumn.class)){
                    ReflectionUtils.makeAccessible(field);
                    Object fieldValue=ReflectionUtils.getField(field, record);
                    String stringValue=fieldValue==null?"":fieldValue.toString();
                    row.add(stringValue);
                }
            }
            rows.add(row.toArray(Object[]::new));
        }

        return rows;
    }

}
