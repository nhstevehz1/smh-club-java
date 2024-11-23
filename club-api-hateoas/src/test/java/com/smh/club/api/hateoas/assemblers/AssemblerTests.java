package com.smh.club.api.hateoas.assemblers;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

import java.util.List;

public abstract class AssemblerTests {

    protected <T> Page<T> createPage(List<T> content) {
        var pageable = PageRequest.of(0, 10, Sort.Direction.ASC, "id");

        return new PageImpl<>(content, pageable, 100);
    }
}
