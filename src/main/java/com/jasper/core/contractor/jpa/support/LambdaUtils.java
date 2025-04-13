/*
 * Copyright (c) All rights Reserved, Designed By Willie Chen
 *
 * @author: Willie Chen
 * @date:   2024/8/23 09:38
 * Note: this content is limited to internal circulation of the company and is not allowed to be leaked or used for other commercial purposes
 */

package com.jasper.core.contractor.jpa.support;


import com.jasper.core.contractor.jpa.support.impl.IdeaProxyLambdaMeta;
import com.jasper.core.contractor.jpa.support.impl.ReflectLambdaMeta;
import org.springframework.util.ReflectionUtils;

import java.lang.invoke.SerializedLambda;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * Lambda Utils
 *
 * @author Willie Chen
 */
public class LambdaUtils {
    private static final Map<SFunction<?, ?>, LambdaMeta> META_CACHE = new HashMap<>();

    private LambdaUtils() {

    }

    /**
     * 该缓存可能会在任意不定的时间被清除
     *
     * @param func 需要解析的 lambda 对象
     * @param <T>  类型，被调用的 Function 对象的目标类型
     * @return 返回解析后的结果
     */
    public static <T> LambdaMeta extract(SFunction<T, ?> func) {
        LambdaMeta cached = META_CACHE.get(func);
        if (cached == null) {
            // 1. IDEA 调试模式下 lambda 表达式是一个代理
            if (func instanceof Proxy proxy) {
                cached = new IdeaProxyLambdaMeta(proxy);
            } else {
                // 2. 反射读取
                try {
                    Method method = func.getClass().getDeclaredMethod("writeReplace");
                    ReflectionUtils.makeAccessible(method);
                    cached = new ReflectLambdaMeta((SerializedLambda) method.invoke(func));
                } catch (Exception e) {
                    // 3. 反射失败使用序列化的方式读取
                    throw new IllegalStateException(e);
                }
            }
        }

        META_CACHE.put(func, cached);
        return cached;
    }

    public static Method findMethod(String name, Class<?> clazz) {
        Method[] methods = ReflectionUtils.getAllDeclaredMethods(clazz);
        for (Method method : methods) {
            if (Objects.equals(method.getName(), name)) {
                return method;
            }
        }
        return null;
    }
}
