package com.smh.club.api.hateoas.services;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.hateoas.PagedModel;

import java.util.List;

public abstract class ServiceTests {

    protected <T> Page<T> createEntityPage(List<T> list, Pageable pageable, int total) {
        return new PageImpl<>(list, pageable, total);
    }

    protected <T> PagedModel<T> createdPageModel(List<T> content ) {;
        PagedModel.PageMetadata md =
            new PagedModel.PageMetadata(content.size(), 5, 200,  20) ;

        return PagedModel.of(content, md);
    }
}
