/*
 * Copyright (c) All rights Reserved, Designed By Willie Chen
 *
 * @author: Willie Chen
 * @date:   2024/8/23 09:38
 * Note: this content is limited to internal circulation of the company and is not allowed to be leaked or used for other commercial purposes
 */

package com.jasper.core.contractor.jpa.query;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

/**
 * Pageable Helper
 *
 * @author Willie Chen
 */
@Slf4j
public class PageableHelper {
    private PageableHelper() {

    }

    /**
     * Build pageable object from request
     *
     * @param request Page Request
     * @param <T>     Model Type
     * @return Pageable
     */
    @SuppressWarnings("ALL")
    public static <T> Pageable getPageable(Class entityClass, PaginationRequest paginationRequest, SortRequest sortRequest) {
        Integer pageNo = paginationRequest.getPage();
        Integer pageSize = paginationRequest.getSize();
        String orderBy = sortRequest.getSortField();
        OrderType orderType = sortRequest.getSortDirection();
        if (pageNo == null) {
            pageNo = 1;
        } else if (pageNo < 0) {
            throw new IllegalArgumentException("Invalid pageNo value: " + pageNo);
        }
        if (pageSize == null) {
            pageSize = 10;
        } else if (pageSize < 0) {
            throw new IllegalArgumentException("Invalid pageSize value: " + pageNo);
        }
        Sort sort = getSort(entityClass, sortRequest);
        return PageRequest.of(pageNo - 1, pageSize, sort);

    }

    @SuppressWarnings("ALL")
    public static Sort getSort(Class entityClass, SortRequest sortRequest) {
        Sort sort = Sort.unsorted();

        String orderBy = sortRequest.getSortField();
        OrderType orderType = sortRequest.getSortDirection();
        if (StringUtils.isNotEmpty(orderBy)) {
            String[] fields = orderBy.split(",");
            for (String field : fields) {
                if (!existsField(entityClass, field)) {
                    throw new IllegalArgumentException("Invalid sort field value: " + field);
                }
            }
            sort = Sort.by(fields);

            if (orderType != null) {
                Sort.Direction direction = Sort.Direction.valueOf(orderType.name());
                sort = Sort.by(direction, fields);
            }

        }
        return sort;
    }

    /**
     * Check field exists in class
     *
     * @param clazz     Class
     * @param fieldName Field Name
     * @return true or false
     */
    private static boolean existsField(Class<?> clazz, String fieldName) {
        try {
            clazz.getDeclaredField(fieldName);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
