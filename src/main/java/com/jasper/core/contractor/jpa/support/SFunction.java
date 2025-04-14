/*
 * Copyright (c) All rights Reserved, Designed By Willie Chen
 *
 * @author: Willie Chen
 * @date:   2024/8/23 09:38
 * Note: this content is limited to internal circulation of the company and is not allowed to be leaked or used for other commercial purposes
 */

package com.jasper.core.contractor.jpa.support;

import java.io.Serializable;
import java.util.function.Function;

/**
 * Extends from java.util.function.Function
 *
 * @param <T> Input
 * @param <R> Return
 * @author Willie Chen
 */
@FunctionalInterface
public interface SFunction<T, R> extends Function<T, R>, Serializable {
}

