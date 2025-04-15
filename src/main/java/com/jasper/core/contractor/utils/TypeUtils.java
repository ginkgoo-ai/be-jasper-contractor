/*
 * Copyright (c) All rights Reserved, Designed By Willie Chen
 *
 * @author: Willie Chen
 * @date:   2024/8/23 09:38
 * Note: this content is limited to internal circulation of the company and is not allowed to be leaked or used for other commercial purposes
 */

package com.jasper.core.contractor.utils;


import com.ginkgooai.core.common.exception.InternalServiceException;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.lang.reflect.WildcardType;

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


}
