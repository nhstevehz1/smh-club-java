package com.smh.club.api.rest.services.services;

import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

public abstract class ServiceTests {

    protected <T> Page<T> createPage(List<T> list, Pageable pageable, int total) {
        return new PageImpl<>(list, pageable, total);
    }
}
