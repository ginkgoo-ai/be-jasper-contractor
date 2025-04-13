/*
 * Copyright (c) All rights Reserved, Designed By Willie Chen
 *
 * @author: Willie Chen
 * @date:   2024/8/23 09:38
 * Note: this content is limited to internal circulation of the company and is not allowed to be leaked or used for other commercial purposes
 */

package com.jasper.core.contractor.utils;

import org.springframework.context.ApplicationContext;

/**
 * 上下文工具类
 */
public class ApplicationContextUtils {
    /**
     * spring 上下文
     */
    private static ApplicationContext applicationContext;

    private ApplicationContextUtils() {

    }

    /**
     * 获取 spring 上下文
     *
     * @return spring 上下文
     */
    public static ApplicationContext get() {
        return applicationContext;
    }

    public static void set(ApplicationContext context) {
        applicationContext = context;
    }


}
