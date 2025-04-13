/*
 * Copyright (c) All rights Reserved, Designed By Willie Chen
 *
 * @author: Willie Chen
 * @date:   2024/8/23 09:38
 * Note: this content is limited to internal circulation of the company and is not allowed to be leaked or used for other commercial purposes
 */

package com.jasper.core.contractor.jpa.query;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.domain.Page;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

/**
 * Page result DTO
 *
 * @param <T> Model Type
 * @author Willie Chen
 */
@Setter
@Getter
public class PageResult<T> {
    private int pageNo;
    private int pageSize;
    private long totalPage;
    private long totalSize;
    private List<T> rows;

    public PageResult(int pageNo, int pageSize, long totalPage, long totalSize, List<T> rows) {
        this.pageNo = pageNo;
        this.pageSize = pageSize;
        this.totalPage = totalPage;
        this.totalSize = totalSize;
        this.rows = rows;
    }

    public PageResult(Page<T> page) {
        this.pageNo = page.getNumber() + 1;
        this.pageSize = page.getSize();
        this.totalPage = page.getTotalPages();
        this.totalSize = page.getTotalElements();
        this.rows = page.get().toList();
    }

    public <E> PageResult(Page<E> page, Function<E, T> rowConvertor) {
        this.rows = page.get().map(rowConvertor).toList();
        this.pageNo = page.getNumber() + 1;
        this.pageSize = page.getSize();
        this.totalPage = page.getTotalPages();
        this.totalSize = page.getTotalElements();
    }

    public PageResult() {
        rows = new ArrayList<>();
        pageNo = 0;
        pageSize = 0;
        totalPage = 0L;
        totalSize = 0L;
    }

}
