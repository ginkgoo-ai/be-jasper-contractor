/*
 * Copyright (c) All rights Reserved, Designed By Willie Chen
 *
 * @author: Willie Chen
 * @date:   2024/8/23 09:38
 * Note: this content is limited to internal circulation of the company and is not allowed to be leaked or used for other commercial purposes
 */

package com.jasper.core.contractor.jpa.support.impl;

import com.jasper.core.contractor.jpa.support.LambdaMeta;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.ReflectionUtils;

import java.lang.invoke.SerializedLambda;
import java.lang.reflect.Field;

/**
 * Relefect Lambda Meta
 *
 * @author Willie
 */
@Slf4j
public class ReflectLambdaMeta implements LambdaMeta {
    static {
        Field fieldCapturingClass;
        try {
            Class<SerializedLambda> aClass = SerializedLambda.class;
            fieldCapturingClass = aClass.getDeclaredField("capturingClass");
            ReflectionUtils.makeAccessible(fieldCapturingClass);
        } catch (Exception e) {
            // Fix JDK version issue: https://gitee.com/baomidou/mybatis-plus/issues/I4A7I5
            log.warn(e.getMessage());

        }

    }

    private final SerializedLambda lambda;

    public ReflectLambdaMeta(SerializedLambda lambda) {
        this.lambda = lambda;
    }

    @Override
    public String getImplClassName() {
        return lambda.getImplClass();
    }

    @Override
    public String getImplMethodName() {
        return lambda.getImplMethodName();
    }

}
