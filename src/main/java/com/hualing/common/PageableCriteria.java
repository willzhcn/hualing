package com.hualing.common;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;


public abstract class PageableCriteria {

    protected int page;

    protected int size = 10;

    protected String[] sort;

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public String[] getSort() {
        return sort;
    }

    public void setSort(String[] sort) {
        this.sort = sort;
    }

    public PageRequest buildPageRequest() {

        return sort == null ? PageRequest.of(page, size) : PageRequest.of(page, size, this.parseParameterIntoSort(this.sort, ","));

    }

    public abstract <T> Specification<T> buildSpecification();

    private Sort parseParameterIntoSort(String[] source, String delimiter) {

        List<Sort.Order> allOrders = new ArrayList<Sort.Order>();

        for (String part : source) {

            if (part == null) {
                continue;
            }

            String[] elements = part.split(delimiter);
            Sort.Direction direction = elements.length == 0 ? null : Sort.Direction.fromString(elements[elements.length - 1]);

            for (int i = 0; i < elements.length; i++) {

                if (i == elements.length - 1 && direction != null) {
                    continue;
                }

                String property = elements[i];

                if (!StringUtils.hasText(property)) {
                    continue;
                }

                allOrders.add(new Sort.Order(direction, property));
            }
        }

        return allOrders.isEmpty() ? null : new Sort(allOrders);
    }
}
