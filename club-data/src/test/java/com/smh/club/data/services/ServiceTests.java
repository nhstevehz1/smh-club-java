package com.smh.club.data.services;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.List;

public abstract class ServiceTests {

    protected <T> Page<T> createEntityPage(List<T> list, Pageable pageable, int total) {
        return new PageImpl<>(list, pageable, total);
    }
}
