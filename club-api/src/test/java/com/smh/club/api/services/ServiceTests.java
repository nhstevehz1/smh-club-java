package com.smh.club.api.services;

import com.smh.club.api.request.PageParams;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.List;

public abstract class ServiceTests {

    protected <T> Page<T> createEntityPage(List<T> list, Pageable pageable, int total) {
        return new PageImpl<>(list, pageable, total);
    }

    protected PageParams createPageParam(int pageNumber, int pageSize, Sort.Direction direction, String column) {
        return PageParams.builder()
                .pageSize(pageSize)
                .pageNumber(pageNumber)
                .sortDirection(direction)
                .sortColumn(column)
                .build();
    }
}
