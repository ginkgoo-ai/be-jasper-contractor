/*
 * Copyright (c) All rights Reserved, Designed By Willie Chen
 *
 * @author: Willie Chen
 * @date:   2024/8/23 09:38
 * Note: this content is limited to internal circulation of the company and is not allowed to be leaked or used for other commercial purposes
 */

package com.jasper.core.contractor.utils;

import java.util.Arrays;

/**
 * Array Utils
 *
 * @author Willie Chen
 */
public class ArrayUtils {
    private ArrayUtils() {

    }

    @SuppressWarnings("ALL")
    public static <T> T[] append(T[] array, T newItem) {
        T[] result = Arrays.copyOf(array, array.length + 1);
        result[result.length - 1] = newItem;
        return result;
    }
}
