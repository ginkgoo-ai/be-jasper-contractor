/*
 * Copyright (c) All rights Reserved, Designed By Willie Chen
 *
 * @author: Willie Chen
 * @date:   2024/8/23 09:38
 * Note: this content is limited to internal circulation of the company and is not allowed to be leaked or used for other commercial purposes
 */

package com.jasper.core.contractor.jpa.support.impl;

import com.jasper.core.contractor.jpa.support.LambdaMeta;
import org.springframework.util.ReflectionUtils;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.reflect.Executable;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Proxy;

/**
 * Idea proxy Lambda Meta
 *
 * @author Willi Chen
 */
public class IdeaProxyLambdaMeta implements LambdaMeta {
    private final Class<?> clazz;
    private final String name;

    public IdeaProxyLambdaMeta(Proxy func) {
        InvocationHandler handler = Proxy.getInvocationHandler(func);
        try {
            Field field = handler.getClass().getDeclaredField("val$target");
            ReflectionUtils.makeAccessible(field);
            MethodHandle dmh = (MethodHandle) field.get(handler);
            Executable executable = MethodHandles.reflectAs(Executable.class, dmh);
            clazz = executable.getDeclaringClass();
            name = executable.getName();
        } catch (IllegalAccessException | NoSuchFieldException e) {
            throw new IllegalStateException(e);
        }
    }

    @Override
    public String getImplMethodName() {
        return name;
    }

    @Override
    public String getImplClassName() {
        return clazz.getName();
    }

    @Override
    public String toString() {
        return clazz.getSimpleName() + "::" + name;
    }

}
