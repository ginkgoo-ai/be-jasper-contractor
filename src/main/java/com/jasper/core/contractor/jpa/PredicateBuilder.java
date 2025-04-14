/*
 * Copyright (c) All rights Reserved, Designed By Willie Chen
 *
 * @author: Willie Chen
 * @date:   2024/8/23 09:38
 * Note: this content is limited to internal circulation of the company and is not allowed to be leaked or used for other commercial purposes
 */

package com.jasper.core.contractor.jpa;

import com.jasper.core.contractor.jpa.support.*;
import com.jasper.core.contractor.utils.StringTools;
import io.hypersistence.utils.hibernate.type.json.JsonBlobType;
import io.hypersistence.utils.hibernate.type.json.JsonType;
import jakarta.persistence.criteria.*;
import lombok.Setter;
import org.hibernate.query.BindableType;
import org.hibernate.query.sqm.NodeBuilder;
import org.hibernate.query.sqm.internal.SqmCriteriaNodeBuilder;
import org.hibernate.query.sqm.tree.expression.SqmExpression;
import org.hibernate.query.sqm.tree.expression.ValueBindJpaCriteriaParameter;
import org.hibernate.sql.ast.tree.expression.CastTarget;
import org.hibernate.type.StandardBasicTypes;
import org.hibernate.type.descriptor.jdbc.JsonAsStringJdbcType;
import org.springframework.util.Assert;

import java.util.Arrays;
import java.util.List;

/**
 * Predicate Builder
 *
 * @param <T> Entity Type
 * @author Willie Chen
 */
@Setter
public class PredicateBuilder<T> {
    private final Root<T> root;
    private CriteriaBuilder builder;
    private String attributeName;

    public PredicateBuilder(CriteriaBuilder builder, Root<T> root, SFunction<T, ?> attribute) {
        this.builder = builder;
        this.root = root;
        if (attribute != null) {
            this.attributeName = getFieldName(attribute);
        }
    }

    private String getFieldName(SFunction<T, ?> attribute) {
        LambdaMeta meta = LambdaUtils.extract(attribute);
        return PropertyNamer.methodToProperty(meta.getImplMethodName());
    }

    public PredicateBuilder<T> when(SFunction<T, ?> attribute) {

        return new PredicateBuilder<>(builder, root, attribute);
    }

    public PredicateBuilder<T> when(String attributeName) {
        PredicateBuilder<T> pb = new PredicateBuilder<>(builder, root, null);
        pb.attributeName = attributeName;
        return pb;
    }

    public Predicate eq(Object value) {
        Assert.notNull(value, "value cannot be null");
        return builder.equal(root.get(attributeName), value);
    }

    public <V extends Comparable<? super V>> Predicate gte(V value) {
        Assert.notNull(value, "value cannot be null");
        return builder.greaterThanOrEqualTo(root.get(attributeName), value);
    }

    public <V extends Comparable<? super V>> Predicate gt(V value) {
        Assert.notNull(value, "value cannot be null");
        return builder.greaterThan(root.get(attributeName), value);
    }

    public <V extends Comparable<? super V>> Predicate lt(V value) {
        Assert.notNull(value, "value cannot be null");
        return builder.lessThan(root.get(attributeName), value);
    }

    public <V extends Comparable<? super V>> Predicate lte(V value) {
        Assert.notNull(value, "value cannot be null");
        return builder.lessThanOrEqualTo(root.get(attributeName), value);
    }

    public <V extends Comparable<? super V>> Predicate isTrue() {

        return builder.isTrue(root.get(attributeName));
    }

    public <V extends Comparable<? super V>> Predicate isFalse() {

        return builder.isFalse(root.get(attributeName));
    }

    public <V extends Comparable<? super V>> Predicate isNull() {

        return builder.isNull(root.get(attributeName));
    }

    public <V extends Comparable<? super V>> Predicate isNotNull() {
        return builder.isNotNull(root.get(attributeName));
    }

    public <V extends Comparable<? super V>> Predicate between(V v1, V v2) {
        Assert.notNull(v1, "v1 cannot be null");
        Assert.notNull(v2, "v2 cannot be null");
        return builder.between(root.get(attributeName), v1, v2);
    }

    public Predicate ne(Object value) {
        return notEqual(value);
    }

    public Predicate notEqual(Object value) {
        Assert.notNull(value, "value cannot be null");
        return builder.notEqual(root.get(attributeName), value);
    }

    public Predicate in(List<?> list) {
        Assert.notNull(list, "list cannot be null");
        Assert.notEmpty(list, "list cannot be empty");
        return builder.in(root.get(attributeName)).value(list);
    }

    public Predicate in(Object... args) {
        Assert.notNull(args, "args cannot be null");
        Assert.notEmpty(args, "args cannot be empty");
        return builder.in(root.get(attributeName)).value(Arrays.asList(args));
    }

    public Predicate like(String pattern) {
        Assert.notNull(pattern, "value cannot be empty");
        return builder.like(root.get(attributeName), StringTools.likePattern(pattern));
    }
    public Predicate jsonContains(String value) {
        Assert.notNull(value, "value cannot be empty");

        Expression<JsonBlobType> cast = builder.function("cast", JsonBlobType.class, root.get(attributeName), builder.literal("as varchar(255)"));

        return builder.equal( builder.function("jsonb_contains", Boolean.class, cast, builder.literal(value)),true);
    }
}
